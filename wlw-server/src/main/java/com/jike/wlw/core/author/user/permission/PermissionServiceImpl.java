package com.jike.wlw.core.author.user.permission;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.user.permission.PPermission;
import com.jike.wlw.dao.author.user.permission.PermissionDao;
import com.jike.wlw.service.author.user.permission.*;
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
@RequestMapping(value = "service/permission", produces = "application/json;charset=utf-8")
public class PermissionServiceImpl extends BaseService implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public Permission get(String tenantId, String id) throws BusinessException {
        try {
            PPermission perz = permissionDao.get(PPermission.class, "tenantId", tenantId, "id", id);
            if (perz == null) {
                perz = permissionDao.get(PPermission.class, "tenantId", tenantId, "uuid", id);
            }
            if (perz == null) {
                return null;
            }

            Permission result = new Permission();
            BeanUtils.copyProperties(perz, result);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, PermissionModifyRq modifyRq, String operator) throws BusinessException {

    }

    @TX
    @Override
    public void save(String tenantId, PermissionSaveRq saveRq, String operator) throws BusinessException {
        try {
            if (CollectionUtils.isEmpty(saveRq.getPermissionRqList())) {
                throw new BusinessException("请至少上传一条权限参数");
            }
//            if (StringUtil.isNullOrBlank(saveRq.getRoleId())) {
//                throw new BusinessException("角色id不能为空");
//            } else {
//                //TODO 需要判空role是否存在
//            }

            int i = 1;
            List<PPermission> perzList = new ArrayList<>();
            for (PermissionRq permissionRq : saveRq.getPermissionRqList()) {
                if (StringUtil.isNullOrBlank(permissionRq.getName())) {
                    throw new BusinessException("权限配置名称不能为空");
                }
                if (StringUtil.isNullOrBlank(permissionRq.getAppId())) {
                    throw new BusinessException("权限配置appId不能为空");
                }
                if (StringUtil.isNullOrBlank(permissionRq.getId())) {
                    throw new BusinessException("权限配置id不能为空");
                }
                if (StringUtil.isNullOrBlank(permissionRq.getGroupId())) {
                    throw new BusinessException("权限配置分组id不能为空");
                }

                PPermission perz = new PPermission();
                BeanUtils.copyProperties(permissionRq, perz);
                perz.setTenantId(tenantId);
//                perz.setRoleId(saveRq.getRoleId());
                perz.onCreated(operator);

                perzList.add(perz);
                i++;
            }

//            permissionDao.removeByRoleId(saveRq.getRoleId());
            permissionDao.save(perzList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Permission> query(String tenantId, PermissionFilter filter) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PPermission> list = permissionDao.query(filter);
            long total = permissionDao.getCount(filter);

            List<Permission> result = new ArrayList<>();
            for (PPermission perz : list) {
                Permission permission = new Permission();
                BeanUtils.copyProperties(perz, permission);

                result.add(permission);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
