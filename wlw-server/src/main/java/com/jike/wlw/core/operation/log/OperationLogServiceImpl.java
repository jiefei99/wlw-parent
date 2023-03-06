package com.jike.wlw.core.operation.log;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jpa.api.entity.Parts;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.AuthDao;
import com.jike.wlw.dao.author.user.role.PRole;
import com.jike.wlw.dao.operation.log.OperationLogDao;
import com.jike.wlw.dao.operation.log.POperationLog;
import com.jike.wlw.dao.operation.log.item.PRolePermissionMenuOperationLogItem;
import com.jike.wlw.dao.operation.log.item.PRoleUserOperationLogItem;
import com.jike.wlw.dao.operation.log.item.RolePermissionMenuOperationLogItemDao;
import com.jike.wlw.dao.operation.log.item.RoleUserOperationLogItemDao;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import com.jike.wlw.service.author.user.employee.EmployeeService;
import com.jike.wlw.service.author.user.permission.Permission;
import com.jike.wlw.service.author.user.permission.PermissionFilter;
import com.jike.wlw.service.author.user.permission.PermissionService;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.operation.log.OperationLogFilter;
import com.jike.wlw.service.operation.log.OperationLogService;
import com.jike.wlw.service.operation.log.OperationType;
import com.jike.wlw.service.operation.log.item.RolePermissionMenuOperationLogItem;
import com.jike.wlw.service.operation.log.item.RolePermissionMenuOperationLogItemFilter;
import com.jike.wlw.service.operation.log.item.RoleUserOperationLogItem;
import com.jike.wlw.service.operation.log.item.RoleUserOperationLogItemFilter;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Slf4j
@RestController
@ApiModel("操作日志服务实现")
@RequestMapping(value = "service/operation/log", produces = "application/json;charset=utf-8")
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;
    @Autowired
    private RolePermissionMenuOperationLogItemDao rolePermissionMenuOperationLogItemDao;
    @Autowired
    private RoleUserOperationLogItemDao roleUserOperationLogItemDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AuthDao authDao;

    @Override
    public OperationLog get(String id, String tenantId) throws BusinessException {
        try {
            POperationLog perz = operationLogDao.get(POperationLog.class, id,"tenantId", tenantId);
            if (perz == null) {
                perz = operationLogDao.get(POperationLog.class, "id", id,"tenantId", tenantId);
            }
            if (perz == null) {
                return null;
            }
            OperationLog result = convert(perz);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public String create(OperationLog operationLog, String operator, String tenantId) throws BusinessException {
        try {
            validate(operationLog);

            POperationLog perz = operationLogDao.get(POperationLog.class, operationLog.getUuid());
            if (perz != null) {
                throw new BusinessException("操作日志已存在");
            }
            perz = new POperationLog();
            BeanUtils.copyProperties(operationLog, perz, "creator", "created");
            perz.setType(operationLog.getType().name());
            perz.onCreated(operator);
            operationLogDao.save(perz);

            // 保存明细：角色权限菜单
            if (OperationType.ROLE_PERMISSION_MENU == operationLog.getType()) {
                saveRolePermissionMenuOperationLogItem(operationLog, perz);
            }
            // 保存明细：角色用户
            if (OperationType.ROLE_USER == operationLog.getType()) {
                saveRoleUserOperationLogItem(operationLog, perz);
            }

            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<OperationLog> query(OperationLogFilter filter, String tenantId) throws BusinessException {
        try {
            List<POperationLog> operationLogs = operationLogDao.query(filter);
            long total = operationLogDao.getCount(filter);

            List<OperationLog> result = new ArrayList<>();
            for (POperationLog perz : operationLogs) {
                OperationLog operationLog = convert(perz);
                result.add(operationLog);
            }

            if (!StringUtil.isNullOrBlank(filter.getParts())) {
                Parts part = new Parts(filter.getParts());
                if (part.contains(OperationLog.PART_ROLE_PERMISSION_MENU_ITEM)) {
                    fetchRolePermissionMenuItem(tenantId, result);
                }
                if (part.contains(OperationLog.PART_ROLE_USER_ITEM)) {
                    fetchRoleUserItem(tenantId, result);
                }
                if (part.contains(OperationLog.PART_EMPLOYEE)) {
                    fetchEmployee(tenantId, result);
                }
                if (part.contains(OperationLog.PART_ROLE)) {
                    fetchRole(result);
                }
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * P对象转换为A对象
     */
    private OperationLog convert(POperationLog perz) {

        OperationLog result = new OperationLog();
        BeanUtils.copyProperties(perz, result);
        result.setType(OperationType.valueOf(perz.getType()));

        return result;
    }

    /**
     * 参数校验
     */
    private void validate(OperationLog operationLog) {
        if (StringUtil.isNullOrBlank(operationLog.getRelationId())) {
            throw new BusinessException("关联ID不能为空");
        }
        if (StringUtil.isNullOrBlank(operationLog.getUserId())) {
            throw new BusinessException("用户ID不能为空");
        }
        if (StringUtil.isNullOrBlank(operationLog.getContent())) {
            throw new BusinessException("操作内容不能为空");
        }
        if (operationLog.getType() == null) {
            throw new BusinessException("操作类型不能为空");
        }
    }

    /**
     * 批量创建参数校验
     */
    private void batchValidate(List<OperationLog> operationLogList) {
        for (OperationLog operationLog : operationLogList) {
            if (StringUtil.isNullOrBlank(operationLog.getRelationId())) {
                throw new BusinessException("关联ID不能为空");
            }
            if (StringUtil.isNullOrBlank(operationLog.getUserId())) {
                throw new BusinessException("用户ID不能为空");
            }
            if (StringUtil.isNullOrBlank(operationLog.getContent())) {
                throw new BusinessException("操作内容不能为空");
            }
            if (operationLog.getType() == null) {
                throw new BusinessException("操作类型不能为空");
            }
        }
    }

    /**
     * 保存角色权限菜单操作日志明细
     */
    private void saveRolePermissionMenuOperationLogItem(OperationLog operationLog, POperationLog perz) throws Exception {
        List<PRolePermissionMenuOperationLogItem> itemList = new ArrayList<>();
        for (RolePermissionMenuOperationLogItem item : operationLog.getRolePermissionMenuOperationLogItemList()) {

            PRolePermissionMenuOperationLogItem pitem = new PRolePermissionMenuOperationLogItem();
            BeanUtils.copyProperties(item, pitem);
            pitem.setOperationLogId(perz.getUuid());
            pitem.setOldRolePermissionMenuListJson(JsonUtil.objectToJson(item.getRolePermissionMenuList()));
            itemList.add(pitem);
        }
        if (!CollectionUtils.isEmpty(itemList)) {
            operationLogDao.save(itemList);
        }
    }

    /**
     * 保存角色用户操作日志明细
     */
    private void saveRoleUserOperationLogItem(OperationLog operationLog, POperationLog perz) throws Exception {
        List<PRoleUserOperationLogItem> itemList = new ArrayList<>();
        for (RoleUserOperationLogItem item : operationLog.getRoleUserOperationLogItemList()) {

            PRoleUserOperationLogItem pitem = new PRoleUserOperationLogItem();
            BeanUtils.copyProperties(item, pitem);
            pitem.setOperationLogId(perz.getUuid());
            itemList.add(pitem);
        }
        if (!CollectionUtils.isEmpty(itemList)) {
            operationLogDao.save(itemList);
        }
    }

    /**
     * 获取角色权限菜单操作日志明细
     */
    private void fetchRolePermissionMenuItem(String tenantId, List<OperationLog> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        List<String> operationLogIds = new ArrayList<>();
        for (OperationLog operationLog : result) {
            operationLogIds.add(operationLog.getUuid());
        }

        // 查询角色权限菜单操作日志明细
        RolePermissionMenuOperationLogItemFilter filter = new RolePermissionMenuOperationLogItemFilter();
        filter.setOperationLogIdIn(operationLogIds);
        List<PRolePermissionMenuOperationLogItem> list = rolePermissionMenuOperationLogItemDao.query(filter);

        Map<String, List<RolePermissionMenuOperationLogItem>> itemMap = new HashMap<>();
        List<String> permissionIds = new ArrayList<>();
        for (PRolePermissionMenuOperationLogItem perz : list) {
            // P对象转换为A对象
            RolePermissionMenuOperationLogItem item = new RolePermissionMenuOperationLogItem();
            BeanUtils.copyProperties(perz, item);
            if (!StringUtil.isNullOrBlank(perz.getOldRolePermissionMenuListJson())) {
                item.setRolePermissionMenuList(JsonUtil.jsonToArrayList(perz.getOldRolePermissionMenuListJson(), RolePermissionMenu.class));
            }
            // Map赋值
            if (!itemMap.containsKey(item.getOperationLogId())) {
                itemMap.put(item.getOperationLogId(), new ArrayList<>());
            }
            List<RolePermissionMenuOperationLogItem> itemList = itemMap.get(item.getOperationLogId());
            itemList.add(item);

            for (RolePermissionMenu rolePermissionMenu : item.getRolePermissionMenuList()) {
                permissionIds.add(rolePermissionMenu.getPermissionId());
            }
        }
        // 查询权限
        PermissionFilter permissionFilter = new PermissionFilter();
        permissionFilter.setIdIn(permissionIds);
        permissionFilter.addOrder("id", true);
        List<Permission> permissionList = permissionService.query(tenantId, permissionFilter).getData();

        Map<String, Permission> permissionMap = new HashMap<>();
        for (Permission permission : permissionList) {
            if (!permissionMap.containsKey(permission.getId())) {
                permissionMap.put(permission.getId(), permission);
            }
        }

        // 设置返回结果
        for (OperationLog operationLog : result) {
            operationLog.setRolePermissionMenuOperationLogItemList(itemMap.get(operationLog.getUuid()));

            String oldRolePermissionMenu = null;
            String newRolePermissionMenu = null;
            for (RolePermissionMenuOperationLogItem item : operationLog.getRolePermissionMenuOperationLogItemList()) {
                for (RolePermissionMenu rolePermissionMenu : item.getRolePermissionMenuList()) {
                    Permission permission = permissionMap.get(rolePermissionMenu.getPermissionId());
                    if (permission != null) {
                        oldRolePermissionMenu = oldRolePermissionMenu == null ? permission.getName() : oldRolePermissionMenu + "," + permission.getName();
                    }
                }
            }
            operationLog.setOldRolePermissionMenu(oldRolePermissionMenu);
            operationLog.setNewRolePermissionMenu(newRolePermissionMenu);
        }
    }

    /**
     * 获取角色用户操作日志明细
     */
    private void fetchRoleUserItem(String tenantId, List<OperationLog> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        List<String> operationLogIds = new ArrayList<>();

        Map<String, OperationLog> operationLogMap = new HashMap<>();
        for (OperationLog operationLog : result) {
            operationLogIds.add(operationLog.getUuid());

            if (!operationLogMap.containsKey(operationLog.getUuid())) {
                operationLogMap.put(operationLog.getUuid(), operationLog);
            }
        }

        // 查询角色权限菜单操作日志明细
        RoleUserOperationLogItemFilter filter = new RoleUserOperationLogItemFilter();
        filter.setOperationLogIdIn(operationLogIds);
        List<PRoleUserOperationLogItem> list = roleUserOperationLogItemDao.query(filter);

        Map<String, List<RoleUserOperationLogItem>> itemMap = new HashMap<>();
        List<String> employeeIds = new ArrayList<>();
        for (PRoleUserOperationLogItem perz : list) {
            employeeIds.add(perz.getUserId());
            // P对象转换为A对象
            RoleUserOperationLogItem item = new RoleUserOperationLogItem();
            BeanUtils.copyProperties(perz, item);

            if (!itemMap.containsKey(item.getOperationLogId())) {
                itemMap.put(item.getOperationLogId(), new ArrayList<>());
            }
            List<RoleUserOperationLogItem> itemList = itemMap.get(item.getOperationLogId());
            itemList.add(item);
        }

        // 获取员工信息
        EmployeeFilter employeeFilter = new EmployeeFilter();
        employeeFilter.setIdIn(employeeIds);
        employeeFilter.setParts("user");
        List<Employee> employeeList = employeeService.query(tenantId, employeeFilter).getData();

        Map<String, Employee> employeeMap = new HashMap<>();
        for (Employee employee : employeeList) {
            if (!employeeMap.containsKey(employee.getUuid())) {
                employeeMap.put(employee.getUuid(), employee);
            }
        }
        // 数据处理：获取 角色用户操作信息
        Map<String, String> roleUserOperationInfoMap = new HashMap<>();
        for (PRoleUserOperationLogItem perz : list) {
            if (!roleUserOperationInfoMap.containsKey(perz.getOperationLogId())) {
                OperationLog operationLog = operationLogMap.get(perz.getOperationLogId());
                String infoHead = null;
                if ("新增角色用户".equals(operationLog.getContent())) {
                    infoHead = "添加员工：";
                } else if ("删除角色用户".equals(operationLog.getContent())) {
                    infoHead = "删除员工：";
                }
                roleUserOperationInfoMap.put(perz.getOperationLogId(), infoHead);
            }
            String info = roleUserOperationInfoMap.get(perz.getOperationLogId());
            String name = employeeMap.get(perz.getUserId()) == null ? "" : employeeMap.get(perz.getUserId()).getUser().getName();
            if ("：".equals(info.substring(info.length() - 1))) {
                info = info + name;
            } else {
                info = info + "," + name;
            }
            roleUserOperationInfoMap.put(perz.getOperationLogId(), info);
        }

        // 设置返回结果
        for (OperationLog operationLog : result) {
            operationLog.setRoleUserOperationLogItemList(itemMap.get(operationLog.getUuid()));
            operationLog.setRoleUserOperationInfo(roleUserOperationInfoMap.get(operationLog.getUuid()));
        }
    }

    /**
     * 获取员工信息
     */
    private void fetchEmployee(String tenantId, List<OperationLog> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        List<String> employeeIds = new ArrayList<>();
        for (OperationLog operationLog : result) {
            employeeIds.add(operationLog.getUserId());
        }

        // 获取操作人信息
        EmployeeFilter employeeFilter = new EmployeeFilter();
        employeeFilter.setUserIdIn(employeeIds);
        List<Employee> employeeList = employeeService.query(tenantId, employeeFilter).getData();

        Map<String, Employee> employeeMap = new HashMap<>();
        for (Employee employee : employeeList) {
            if (!employeeMap.containsKey(employee.getUserId())) {
                employeeMap.put(employee.getUserId(), employee);
            }
        }
        // 设置返回结果
        for (OperationLog operationLog : result) {
            operationLog.setEmployee(employeeMap.get(operationLog.getUserId()));
        }
    }

    /**
     * 获取角色信息
     */
    private void fetchRole(List<OperationLog> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        List<String> roleIds = new ArrayList<>();
        for (OperationLog operationLog : result) {
            roleIds.add(operationLog.getRelationId());
        }
        // 查询角色
        RoleFilter filter = new RoleFilter();
        filter.setUuidIn(roleIds);
        List<PRole> roleList = authDao.queryRole(filter);

        if (CollectionUtils.isEmpty(roleList)) {
            return;
        }
        // 制作roleMap
        Map<String, Role> roleMap = new HashMap<>();
        for (PRole perz : roleList) {
            Role role = new Role();
            BeanUtils.copyProperties(perz, role);

            if (!roleMap.containsKey(role.getUuid())) {
                roleMap.put(role.getUuid(), role);
            }
        }

        // 返回结果
        for (OperationLog operationLog : result) {
            operationLog.setRole(roleMap.get(operationLog.getRelationId()));
        }
    }

}
