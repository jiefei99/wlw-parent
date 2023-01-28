package com.jike.wlw.core.author.user;

import com.geeker123.rumba.commons.base.FreezeStatus;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.user.PUser;
import com.jike.wlw.dao.author.user.UserDao;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserCreateRq;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserModifyRq;
import com.jike.wlw.service.author.user.UserService;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @author mengchen
 * @date 2022/7/19
 * @apiNote
 */
@Slf4j
@RestController
@ApiModel("用户服务实现")
@RequestMapping(value = "service/user", produces = "application/json;charset=utf-8")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User get(String tenantId, String id) throws BusinessException {
        try {
            Assert.assertArgumentNotNullOrBlank(tenantId, "tenantId");
            Assert.assertArgumentNotNullOrBlank(id, "id");

            PUser perz = userDao.get(PUser.class, "uuid", id, "tenantId", tenantId);
            if (perz == null) {
                return null;
            }
            User result = new User();
            BeanUtils.copyProperties(perz, result);
            result.setUserType(UserType.valueOf(perz.getUserType()));
            result.setStatus(FreezeStatus.valueOf(perz.getStatus()));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String create(String tenantId, UserCreateRq createRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNullOrBlank(tenantId, "tenantId");
            Assert.assertArgumentNotNull(createRq, "createRq");
            Assert.assertArgumentNotNullOrBlank(operator, "operator");

            if (createRq.getUserType() == null) {
                throw new BusinessException("用户类型不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getMobile())) {
                throw new BusinessException("用户手机号不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("用户姓名不能为空");
            }
            PUser perz = userDao.get(PUser.class, "mobile", createRq.getMobile());
            if (perz != null) {
                throw new BusinessException("该手机号已被注册");
            }
            perz = new PUser();
            BeanUtils.copyProperties(createRq, perz);
            perz.setTenantId(tenantId);
            perz.setUserType(createRq.getUserType().name());
            perz.setStatus(FreezeStatus.NORMAL.name());
            perz.onCreated(operator);

            userDao.insertUser(perz);

            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, UserModifyRq modifyRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNullOrBlank(tenantId, "tenantId");
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");
            Assert.assertArgumentNotNullOrBlank(operator, "operator");

            if (StringUtil.isNullOrBlank(modifyRq.getUuid())) {
                throw new BusinessException("用户UUID不能为空");
            }
            if (StringUtil.isNullOrBlank(modifyRq.getMobile())) {
                throw new BusinessException("用户手机号不能为空");
            }
            PUser perz = userDao.get(PUser.class, "uuid", modifyRq.getUuid(), "tenantId", tenantId);
            if (perz == null) {
                throw new BusinessException("该用户不存在或已删除");
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getSex())) {
                perz.setSex(modifyRq.getSex());
            }
            if (modifyRq.getStatus() != null) {
                perz.setStatus(modifyRq.getStatus().name());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getMobile())) {
                perz.setMobile(modifyRq.getMobile());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getHeadImage())) {
                perz.setHeadImage(modifyRq.getHeadImage());
            }
            perz.setRemark(modifyRq.getRemark());

            perz.onModified(operator);
            userDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }

    }

    @Override
    public PagingResult<User> query(String tenantId, UserFilter filter) throws BusinessException {
        try {
            Assert.assertArgumentNotNullOrBlank(tenantId, "tenantId");
            Assert.assertArgumentNotNull(filter, "filter");

            List<PUser> list = userDao.query(filter);
            long total = userDao.getCount(filter);

            List<User> result = new ArrayList<>();
            for (PUser perz : list) {
                User user = new User();
                BeanUtils.copyProperties(perz, user);
                user.setUserType(UserType.valueOf(perz.getUserType()));
                user.setStatus(FreezeStatus.valueOf(perz.getStatus()));
                result.add(user);
            }
            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }
}
