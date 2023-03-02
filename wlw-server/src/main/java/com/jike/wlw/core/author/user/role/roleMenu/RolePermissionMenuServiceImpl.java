package com.jike.wlw.core.author.user.role.roleMenu;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.author.user.role.PRoleMenu;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.auth.RolePermissionMenu;
import com.jike.wlw.service.author.user.role.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "service/roleMenu", produces = "application/json;charset=utf-8")
public class RolePermissionMenuServiceImpl extends BaseService implements RolePermissionMenuService {

    @Autowired
    private RolePermissionMenuDao rolePermissionMenuDao;
    @Autowired
    private RoleService roleService;

    @Override
    public void saveRolePermissionMenus(String tenantId, RoleMenuCreateRq createRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getRoleId())) {
                throw new BusinessException("角色id不能为空");
            } else {
                Role role = roleService.get(tenantId, createRq.getRoleId());
                if (role == null) {
                    throw new BusinessException("指定角色不存在或已删除");
                }
            }

            //删除该角色之前的所有权限
            rolePermissionMenuDao.deleteByRoleId(createRq.getRoleId());

            //如果权限菜单配置不为空，保存配置，如果为空，就不再保存
            if (!CollectionUtils.isEmpty(createRq.getRoleMenuList())) {
                List<PRoleMenu> perzList = new ArrayList<>();
                for (RoleMenu roleMenu : createRq.getRoleMenuList()) {
                    PRoleMenu perz = new PRoleMenu();
                    BeanUtils.copyProperties(roleMenu, perz);
                    perz.setTenantId(tenantId);
                    perzList.add(perz);
                }

                rolePermissionMenuDao.save(perzList);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<RolePermissionMenu> query(String tenantId, AuthFilter filter) throws BusinessException {
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
}
