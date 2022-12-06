package com.jike.wlw.core.author;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.AuthDao;
import com.jike.wlw.dao.author.user.role.PUserRole;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.AuthService;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserService;
import com.jike.wlw.service.author.user.role.UserRole;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 修改历史：
 * 2019/8/27 17:20- sufengjia - 创建。
 */
@Slf4j
@RestController
@ApiModel("授权服务实现")
public class AuthServiceImpl extends BaseService implements AuthService {

    @Autowired
    private AuthDao authDao;
    @Autowired
    private UserService userService;


    @TX
    @Override
    public void saveUserRoles(String userId, String roleIdsJson) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(userId, "userId");

            List<String> roleIds = JsonUtil.jsonToArrayList(roleIdsJson, String.class);

            authDao.saveUserRole(userId, roleIds);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void saveUsersRole(String userIdsJson, String roleId) throws BusinessException {
        try {
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
            for (String userId : userIds) {
                PUserRole perz = new PUserRole();
                perz.setRoleId(roleId);
                perz.setUserId(userId);

                result.add(perz);
            }

            authDao.save(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void removeUsersRole(String userIdsJson, String roleId) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(userIdsJson)) {
                throw new BusinessException("用户ids字符串不能为空");
            }
            if (StringUtil.isNullOrBlank(roleId)) {
                throw new BusinessException("角色id不能为空");
            }

            List<String> userIds = JsonUtil.jsonToArrayList(userIdsJson, String.class);

            String ids = StringUtils.join(userIds, "','");

            authDao.batchRemoveUserRole(roleId, ids);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserRole> getUserRolesByUserId(String userId) throws BusinessException {
        try {
            List<UserRole> result = authDao.getUserRolesByUserId(userId);

            return result;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<UserRole> query(AuthFilter filter) throws BusinessException {
        try {
            List<PUserRole> list = authDao.query(filter);
            long count = authDao.getCount(filter);

            List<UserRole> result = new ArrayList<>();
            for (PUserRole perz : list) {
                UserRole userRole = new UserRole();
                BeanUtils.copyProperties(perz, userRole);

                result.add(userRole);
            }

            fetchUser(result);

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private void fetchUser(List<UserRole> userRoleList) {
        if (CollectionUtils.isEmpty(userRoleList)) {
            return;
        }

        List<String> userIds = new ArrayList<>();
        for (UserRole userRole : userRoleList) {
            userIds.add(userRole.getUserId());
        }

        UserFilter filter = new UserFilter();
        filter.setUserIdIn(userIds);
        List<User> userList = userService.query(filter).getData();

        Map<String, User> userMapper = new HashMap<>();
        for (User user : userList) {
            if (!userMapper.containsKey(user.getUuid())) {
                userMapper.put(user.getUuid(), user);
            }
        }

        for (UserRole userRole : userRoleList) {
            if (userMapper.containsKey(userRole.getUserId())) {
                userRole.setUser(userMapper.get(userRole.getUserId()));
            }
        }
    }

}
