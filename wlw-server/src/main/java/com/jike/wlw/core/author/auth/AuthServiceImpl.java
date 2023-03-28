/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： tms-api
 * 文件名： AuthService.java
 * 模块说明：
 * 修改历史：
 * 2018年4月16日 - subinzhu - 创建。
 */
package com.jike.wlw.core.author.auth;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.AuthDao;
import com.jike.wlw.dao.author.user.role.PRole;
import com.jike.wlw.dao.author.user.role.PUserRole;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.AuthService;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import com.jike.wlw.service.author.user.employee.EmployeeService;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.author.user.role.UserRole;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.operation.log.OperationLogService;
import com.jike.wlw.service.operation.log.OperationType;
import com.jike.wlw.service.operation.log.item.RoleUserOperationLogItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权服务
 *
 * @author subinzhu
 */
@Slf4j
@RestController
@RequestMapping(value = "service/auth", produces = "application/json;charset=utf-8")
public class AuthServiceImpl extends BaseService implements AuthService {

    @Autowired
    private AuthDao authDao;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OperationLogService operationLogService;

    @TX
    @Override
    public void saveRole(Role role, String tenantId, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(role.getName())) {
                throw new BusinessException("角色名称不能为空");
            }
            PRole perz = null;
            if (StringUtil.isNullOrBlank(role.getUuid())) {
                perz = authDao.get(PRole.class, "name", role.getName());
                if (perz != null) {
                    throw new BusinessException("当前角色名称已存在，无法创建");
                }
            } else {
                perz = authDao.get(PRole.class, role.getUuid());
            }

            if (perz == null) {
                perz = new PRole();
                BeanUtils.copyProperties(role, perz);
                perz.setTenantId(tenantId);
                perz.onCreated(operator);
            } else if (!StringUtil.isNullOrBlank(perz.getUuid())){
                BeanUtils.copyProperties(role, perz, "tenantId", "uuid", "created", "creator", "modified", "modifier");
                perz.onModified(operator);
            }

            authDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void removeRole(String roleId, String tenantId) throws BusinessException {
        try {
            authDao.removeRole(roleId, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Role> queryRole(RoleFilter filter, String tenantId) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PRole> roles = authDao.queryRole(filter);
            long total = authDao.getRoleCount(filter);

            List<Role> result = new ArrayList<>();
            for (PRole perz : roles) {
                Role role = new Role();
                BeanUtils.copyProperties(perz, role);
                result.add(role);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void saveUsersRole(String tenantId, String userIdsJson, String roleId, String userId) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户Id不能为空");
            }
            if (StringUtil.isNullOrBlank(userIdsJson)) {
                throw new BusinessException("用户ids字符串不能为空");
            }
            if (StringUtil.isNullOrBlank(roleId)) {
                throw new BusinessException("角色id不能为空");
            }

            List<String> userIds = JsonUtil.jsonToArrayList(userIdsJson, String.class);

            //过滤掉角色中已存在的重复的用户
            AuthFilter filter = new AuthFilter();
            filter.setRoleIdEq(roleId);
            List<PUserRole> pUserRoles = authDao.query(filter);
            if (!CollectionUtils.isEmpty(pUserRoles)) {
                List<String> existUserIds = new ArrayList<>();
                for (PUserRole pUserRole : pUserRoles) {
                    existUserIds.add(pUserRole.getUserId());
                }
                userIds.removeAll(existUserIds);
            }

            if (CollectionUtils.isEmpty(userIds)) {
                return;
            }

            List<PUserRole> result = new ArrayList<>();
            for (String id : userIds) {
                PUserRole perz = new PUserRole();
                perz.setRoleId(roleId);
                perz.setUserId(id);

                result.add(perz);
            }

            authDao.save(result);

            // 保存角色用户操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setType(OperationType.ROLE_USER);
            operationLog.setUserId(userId);
            operationLog.setRelationId(roleId);
            operationLog.setContent("新增角色用户");
            // 明细
            List<RoleUserOperationLogItem> itemList = new ArrayList<>();
            for (String id : userIds) {
                RoleUserOperationLogItem item = new RoleUserOperationLogItem();
                item.setUserId(id);
                itemList.add(item);
            }
            operationLog.setRoleUserOperationLogItemList(itemList);
            operationLogService.create(operationLog, "通过用户列表保存用户角色", tenantId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void saveUserRoles(String tenantId, String userId, String roleIdsJson) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户Id不能为空");
            }
            if (StringUtil.isNullOrBlank(roleIdsJson)) {
                throw new BusinessException("角色ids字符串不能为空");
            }
            if (StringUtil.isNullOrBlank(userId)) {
                throw new BusinessException("用户id不能为空");
            }
            List<String> roleIds = JsonUtil.jsonToArrayList(roleIdsJson, String.class);

            // 过滤掉用户中已存在的重复的角色
            AuthFilter filter = new AuthFilter();
            filter.setUserIdEq(userId);
            List<PUserRole> pUserRoles = authDao.query(filter);
            // 需要删除的角色
            List<String> deleteRoleIds = new ArrayList<>();
            // 需要添加的角色
            List<String> addRoleIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pUserRoles)) {
                for (PUserRole pUserRole : pUserRoles) {
                    if (!roleIds.contains(pUserRole.getRoleId())) {
                        deleteRoleIds.add(pUserRole.getRoleId());
                    } else {
                        addRoleIds.add(pUserRole.getRoleId());
                    }
                }
                roleIds.removeAll(addRoleIds);
            }

            if (CollectionUtils.isEmpty(roleIds) && CollectionUtils.isEmpty(deleteRoleIds)) {
                return;
            }
            authDao.batchRemoveUserRoles(userId, deleteRoleIds);
            // 保存新的角色
            List<PUserRole> result = new ArrayList<>();
            for (String roleId : roleIds) {
                PUserRole pUserRole = new PUserRole();
                pUserRole.setUserId(userId);
                pUserRole.setRoleId(roleId);

                result.add(pUserRole);
            }
            authDao.save(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void removeUsersRole(String tenantId, String userIdsJson, String roleId, String userId) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户Id不能为空");
            }
            if (StringUtil.isNullOrBlank(userIdsJson)) {
                throw new BusinessException("用户ids字符串不能为空");
            }
            if (StringUtil.isNullOrBlank(roleId)) {
                throw new BusinessException("角色id不能为空");
            }
            List<String> userIds = JsonUtil.jsonToArrayList(userIdsJson, String.class);

            authDao.batchRemoveUserRole(roleId, userIds);

            // 删除角色用户操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setType(OperationType.ROLE_USER);
            operationLog.setUserId(userId);
            operationLog.setRelationId(roleId);
            operationLog.setContent("删除角色用户");
            // 明细
            List<RoleUserOperationLogItem> itemList = new ArrayList<>();
            for (String id : userIds) {
                RoleUserOperationLogItem item = new RoleUserOperationLogItem();
                item.setUserId(id);
                itemList.add(item);
            }
            operationLog.setRoleUserOperationLogItemList(itemList);
            operationLogService.create(operationLog, "通过用户列表删除用户角色", tenantId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void removeUserRoles(String tenantId, String userId) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(tenantId)) {
                throw new BusinessException("租户Id不能为空");
            }
            if (StringUtil.isNullOrBlank(userId)) {
                throw new BusinessException("用户id不能为空");
            }
            AuthFilter filter = new AuthFilter();
            filter.setUserIdEq(userId);
            List<PUserRole> pUserRoles = authDao.query(filter);
            List<String> roleIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(pUserRoles)) {
                for (PUserRole pUserRole : pUserRoles) {
                    roleIds.add(pUserRole.getRoleId());
                }
            }

            if (CollectionUtils.isEmpty(roleIds)) {
                return;
            }
            authDao.batchRemoveUserRoles(userId, roleIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserRole> getUserRoles(String tenantId, String userId) throws BusinessException {
        try {
            List<UserRole> result = authDao.getUserRoles(tenantId, userId);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<UserRole> query(String tenantId, AuthFilter filter) throws BusinessException {
        try {
            List<PUserRole> list = authDao.query(filter);
            long count = authDao.getCount(filter);

            List<UserRole> result = new ArrayList<>();
            for (PUserRole perz : list) {
                UserRole userRole = new UserRole();
                BeanUtils.copyProperties(perz, userRole);

                result.add(userRole);
            }

            fetchUser(tenantId, result);

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private void fetchUser(String tenantId, List<UserRole> userRoleList) {
        if (CollectionUtils.isEmpty(userRoleList)) {
            return;
        }

        List<String> userIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            userIds.add(userRole.getUserId());
        }

        EmployeeFilter filter = new EmployeeFilter();
        filter.setUserIdIn(userIds);
        filter.setTenantIdEq(tenantId);
        List<Employee> userList = employeeService.query(tenantId, filter).getData();

        Map<String, Employee> employeeMapper = new HashMap<>();
        for (Employee employee : userList) {
            if (!employeeMapper.containsKey(employee.getUserId())) {
                employeeMapper.put(employee.getUserId(), employee);
            }
        }

        for (UserRole userRole : userRoleList) {
            if (employeeMapper.containsKey(userRole.getUserId())) {
                userRole.setEmployee(employeeMapper.get(userRole.getUserId()));
            }
        }
    }
}
