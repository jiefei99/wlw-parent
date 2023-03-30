package com.jike.wlw.core.author.user.role.roleMenu;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import com.jike.wlw.service.author.auth.RolePermissionMenuCreateRq;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RolePermissionMenuService;
import com.jike.wlw.service.author.user.role.RoleService;
import com.jike.wlw.service.operation.log.OperationLog;
import com.jike.wlw.service.operation.log.OperationLogService;
import com.jike.wlw.service.operation.log.OperationType;
import com.jike.wlw.service.operation.log.item.RolePermissionMenuOperationLogItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "service/rolePermissionMenu", produces = "application/json;charset=utf-8")
public class RolePermissionMenuServiceImpl extends BaseService implements RolePermissionMenuService {

    @Autowired
    private RolePermissionMenuDao rolePermissionMenuDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OperationLogService operationLogService;

    @TX
    @Override
    public void saveRolePermissionMenus(RolePermissionMenuCreateRq createRq, String tenantId) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getRoleId())) {
                throw new BusinessException("角色id不能为空");
            } else {
                Role role = roleService.get(tenantId, createRq.getRoleId());
                if (role == null) {
                    throw new BusinessException("指定角色不存在或已删除");
                }
            }

            // 查询旧权限，保存操作日志需要
            AuthFilter authFilter = new AuthFilter();
            authFilter.setRoleIdEq(createRq.getRoleId());
            authFilter.setTenantIdEq(tenantId);
            List<PRolePermissionMenu> rolePermissionMenuList = rolePermissionMenuDao.query(authFilter);

            List<RolePermissionMenu> oldRolePermissionMenuList = new ArrayList<>();
            for (PRolePermissionMenu perz : rolePermissionMenuList) {
                RolePermissionMenu rolePermissionMenu = convert(perz);
                oldRolePermissionMenuList.add(rolePermissionMenu);
            }

            //删除该角色之前的所有权限
            rolePermissionMenuDao.deleteByRoleId(createRq.getRoleId());

            //如果权限菜单配置不为空，保存配置，如果为空，就不再保存
            if (!CollectionUtils.isEmpty(createRq.getRoleMenuList())) {
                List<PRolePermissionMenu> perzList = new ArrayList<>();
                for (RolePermissionMenu rolePermissionMenu : createRq.getRoleMenuList()) {
                    PRolePermissionMenu perz = new PRolePermissionMenu();
                    BeanUtils.copyProperties(rolePermissionMenu, perz);
                    perz.setTenantId(tenantId);
                    perzList.add(perz);
                }

                rolePermissionMenuDao.save(perzList);
            }

            // 保存操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setType(OperationType.ROLE_PERMISSION_MENU);
            operationLog.setUserId(createRq.getUserId());
            operationLog.setRelationId(createRq.getRoleId());
            operationLog.setContent("角色权限菜单更新");
            RolePermissionMenuOperationLogItem item = new RolePermissionMenuOperationLogItem();
            item.setRolePermissionMenuList(createRq.getRoleMenuList());
            operationLog.setRolePermissionMenuOperationLogItemList(Arrays.asList(item));
            operationLogService.create(operationLog, "保存角色权限菜单", tenantId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<RolePermissionMenu> query(AuthFilter filter, String tenantId) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PRolePermissionMenu> list = rolePermissionMenuDao.query(filter);
            long total = rolePermissionMenuDao.getCount(filter);

            List<RolePermissionMenu> result = new ArrayList<>();
            for (PRolePermissionMenu perz : list) {
                RolePermissionMenu roleMenu = new RolePermissionMenu();
                BeanUtils.copyProperties(perz, roleMenu);

                result.add(roleMenu);
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
    private RolePermissionMenu convert(PRolePermissionMenu perz) {
        RolePermissionMenu roleMenu = new RolePermissionMenu();
        BeanUtils.copyProperties(perz, roleMenu);

        return roleMenu;
    }
}
