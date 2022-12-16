package com.jike.wlw.core.author.user.role.roleMenu;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.author.user.role.PRoleMenu;
import com.jike.wlw.dao.author.user.role.RoleMenuDao;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleService;
import com.jike.wlw.service.author.user.role.RoleMenu;
import com.jike.wlw.service.author.user.role.RoleMenuCreateRq;
import com.jike.wlw.service.author.user.role.RoleMenuService;
import com.jike.wlw.service.author.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class RoleMenuServiceImpl extends BaseService implements RoleMenuService {

    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private RoleService roleService;

    @Override
    public void saveRoleMenus(RoleMenuCreateRq createRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getRoleId())) {
                throw new BusinessException("角色id不能为空");
            } else {
                Role role = roleService.get(createRq.getRoleId());
                if (role == null) {
                    throw new BusinessException("指定角色不存在或已删除");
                }
            }

            //删除该角色之前的所有权限
            roleMenuDao.deleteByRoleId(createRq.getRoleId());


            //如果权限菜单配置不为空，保存配置，如果为空，就不再保存
            if (!CollectionUtils.isEmpty(createRq.getRoleMenuList())) {
                List<PRoleMenu> perzList = new ArrayList<>();
                for (RoleMenu roleMenu : createRq.getRoleMenuList()) {
                    PRoleMenu perz = new PRoleMenu();
                    BeanUtils.copyProperties(roleMenu, perz);

                    perzList.add(perz);
                }

                roleMenuDao.save(perzList);
            }


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<RoleMenu> query(AuthFilter filter) throws BusinessException {
        try {
            List<PRoleMenu> list = roleMenuDao.query(filter);
            long total = roleMenuDao.getCount(filter);

            List<RoleMenu> result = new ArrayList<>();
            for (PRoleMenu perz : list) {
                RoleMenu roleMenu = new RoleMenu();
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
