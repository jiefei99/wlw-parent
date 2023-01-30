package com.jike.wlw.core.author.user.role;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.user.role.PRole;
import com.jike.wlw.dao.author.user.role.RoleDao;
import com.jike.wlw.service.author.user.role.Role;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.author.user.role.RoleService;
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
@RequestMapping(value = "service/role", produces = "application/json;charset=utf-8")
public class RoleServiceImpl extends BaseService implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Role get(String tenantId, String id) throws BusinessException {
        try {
            PRole perz = roleDao.get(PRole.class, "tenantId", tenantId, "uuid", id);
            if (perz == null) {
                return null;
            }

            Role result = new Role();
            BeanUtils.copyProperties(perz, result);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void save(String tenantId, Role role, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(role.getName())) {
                throw new BusinessException("请输入角色名称");
            } else {
                RoleFilter filter = new RoleFilter();
                filter.setNameEq(role.getName());
                List<PRole> query = roleDao.query(filter);

                if (!CollectionUtils.isEmpty(query)) {
                    throw new BusinessException("当前角色名称已存在，无法重复创建");
                }
            }

            PRole perz = new PRole();
            BeanUtils.copyProperties(role, perz);
            perz.setTenantId(tenantId);
            perz.onCreated(operator);

            roleDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, Role role, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(role.getUuid())) {
                throw new BusinessException("角色uuid不能为空");
            }

            PRole perz = roleDao.get(PRole.class, "tenantId", tenantId, "uuid", role.getUuid());
            if (perz == null) {
                throw new BusinessException("指定角色不存在或已删除");
            }

            if (!StringUtil.isNullOrBlank(role.getName())) {
                perz.setName(role.getName());
            }
            if (!StringUtil.isNullOrBlank(role.getRemark())) {
                perz.setRemark(role.getRemark());
            }
            perz.onModified(operator);

            roleDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void delete(String tenantId, String id) throws BusinessException {
        try {
            roleDao.remove(PRole.class, id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Role> query(String tenantId, RoleFilter filter) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PRole> list = roleDao.query(filter);
            long total = roleDao.getCount(filter);

            List<Role> result = new ArrayList<>();
            for (PRole perz : list) {
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
}
