package com.jike.wlw.core.author.user.employee;

import com.enet.base.admin.api.dto.UserDTO;
import com.enet.base.admin.api.dto.UserInfo;
import com.enet.base.admin.api.feign.RemoteUserService;
import com.enet.base.common.core.constant.SecurityConstants;
import com.enet.base.common.core.util.R;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jpa.api.entity.Parts;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.login.account.AccountUserDao;
import com.jike.wlw.dao.author.user.PUser;
import com.jike.wlw.dao.author.user.employee.EmployeeDao;
import com.jike.wlw.dao.author.user.employee.PEmployee;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserCreateRq;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserModifyRq;
import com.jike.wlw.service.author.user.UserService;
import com.jike.wlw.service.author.user.UserType;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.employee.EmployeeCreateAdminRq;
import com.jike.wlw.service.author.user.employee.EmployeeCreateRq;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import com.jike.wlw.service.author.user.employee.EmployeeModifyRq;
import com.jike.wlw.service.author.user.UserModifyStatusRq;
import com.jike.wlw.service.author.user.employee.EmployeeModifyStatusRq;
import com.jike.wlw.service.author.user.employee.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mengchen
 * @date 2022/7/19
 * @apiNote
 */
@Slf4j
@RestController
@RequestMapping(value = "service/employee", produces = "application/json;charset=utf-8")
public class EmployeeServiceImpl extends BaseService implements EmployeeService {
    public static final long AGENT_DEFAULT_DEPT_ID = 1;
    public static final long AGENT_DEFAULT_ROLE_ID = 2;

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private AccountUserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private RemoteUserService remoteUserService;

    @Override
    public Employee get(String tenantId, String userId) throws BusinessException {
        try {
            PEmployee perz = employeeDao.get(PEmployee.class, "userId", userId, "tenantId", tenantId, "isDeleted", 0);
            if (perz == null) {
                return null;
            }
            Employee result = new Employee();
            BeanUtils.copyProperties(perz, result);
            result.setAdmin(perz.getAdmin());
            fetchUser(tenantId, Arrays.asList(result));
            //TODO fetchRole
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String create(String tenantId, EmployeeCreateRq createRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verification(createRq);
            if (StringUtil.isNullOrBlank(createRq.getSex())) {
                throw new BusinessException("性别不能为空");
            }

            PUser pUser = userDao.get(PUser.class, "mobile", createRq.getMobile());
            if (pUser != null) {
                return pUser.getUuid();
            }

            PEmployee perz = new PEmployee();
            //添加base用户
            UserDTO userDTO = new UserDTO();
            //手机号为登录账号
            userDTO.setUsername(createRq.getLoginId());
            // 默认密码123456
            userDTO.setPassword("123456");
            userDTO.setName(createRq.getName());
            userDTO.setDeptId(AGENT_DEFAULT_DEPT_ID);
            //todo 这个租户是存我们的，还是默认使用思钦的1。我感觉是用在思钦注册的租户id，应该不会是默认1
            userDTO.setTenantId(1L);
            List<Long> roles = new ArrayList<>();
            roles.add(AGENT_DEFAULT_ROLE_ID);
            userDTO.setRole(roles);
            R<Boolean> rs = remoteUserService.addUser(userDTO);
            if (rs.getCode() != 0) {
                throw new BusinessException(rs.getMsg());
            }
            //添加代理商用户
            UserInfo sysUser = remoteUserService.info(createRq.getLoginId(), SecurityConstants.FROM_IN).getData();
            UserCreateRq userCreateRq = new UserCreateRq();
            userCreateRq.setUserType(UserType.EMPLOYEE);
            userCreateRq.setMobile(createRq.getMobile());
            userCreateRq.setName(createRq.getName());
            userCreateRq.setSex(createRq.getSex());
            userCreateRq.setUuid(StringUtil.toString(sysUser.getSysUser().getUserId()));
            //用户的uuid拿的就是 思钦 那套账号密码系统的userId（这个userid可能是那张表的uuid，然后账号就在那张表的一个属性）
            String userId = userService.create(tenantId, userCreateRq, operator);
//            perz.setId(idGen(createRq.getTenantId()));
//            while (perz.getId() == null) {
//                perz.setId(idGen(createRq.getTenantId()));
//            }
            perz.setAdmin(false);
            perz.onCreated(operator);
            perz.setUserId(userId);
            perz.setTenantId(createRq.getTenantId());
            employeeDao.save(perz);
            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String createAdmin(String tenantId, EmployeeCreateAdminRq adminRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verification(adminRq);

            PEmployee perz = employeeDao.get(PEmployee.class, "tenantId", adminRq.getTenantId(), "admin", "1");
            if (perz != null) {
                return perz.getUserId();
            }
            UserCreateRq createRq = new UserCreateRq();
            createRq.setUserType(UserType.EMPLOYEE);
            createRq.setName(adminRq.getName());
            createRq.setMobile(adminRq.getMobile());
            String userId = userService.create(tenantId, createRq, operator);

            perz = new PEmployee();
            perz.onCreated(operator);
            perz.setAdmin(true);
            perz.setUserId(userId);
            perz.setTenantId(adminRq.getTenantId());
//            perz.setId(idGen(adminRq.getTenantId()));
            employeeDao.save(perz);

            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, EmployeeModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(modifyRq.getUserId())) {
                throw new BusinessException("员工的用户id不可为空");
            }

            PEmployee perz = employeeDao.get(PEmployee.class, "userId",modifyRq.getUserId(),"tenantId", tenantId, "isDeleted",0);
            if (perz == null) {
                throw new BusinessException("员工不存在");
            }
            UserModifyRq userModifyRq = new UserModifyRq();
            userModifyRq.setUuid(perz.getUserId());
            userModifyRq.setName(modifyRq.getName());
            userModifyRq.setMobile(modifyRq.getMobile());
            userModifyRq.setSex(modifyRq.getSex());
            userModifyRq.setRemark(modifyRq.getRemark());
            userService.modify(tenantId, userModifyRq, operator);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public void modifyStatus(String tenantId, EmployeeModifyStatusRq modifyStatusRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(modifyStatusRq.getUserId())) {
                throw new BusinessException("员工的用户id不可为空");
            }
            PEmployee perz = employeeDao.get(PEmployee.class, "userId",modifyStatusRq.getUserId(),"tenantId", tenantId, "isDeleted",0);
            if (perz == null) {
                throw new BusinessException("员工不存在");
            }
            UserModifyRq userModifyRq = new UserModifyRq();
            userModifyRq.setUuid(perz.getUserId());
            userModifyRq.setStatus(modifyStatusRq.getStatus());
            userService.modify(tenantId, userModifyRq, operator);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public PagingResult<Employee> query(String tenantId, EmployeeFilter filter) throws BusinessException {
        try {
            if (StringUtils.isBlank(tenantId)){
                throw new BusinessException("租户信息不可以为空");
            }
            filter.setTenantIdEq(tenantId);
            List<PEmployee> list = employeeDao.query(filter);
            long total = employeeDao.getCount(filter);

            List<Employee> result = new ArrayList<>();
            for (PEmployee perz : list) {
                Employee employee = new Employee();
                BeanUtils.copyProperties(perz, employee);
                employee.setAdmin(perz.getAdmin());

                result.add(employee);
            }
            if (!StringUtil.isNullOrBlank(filter.getParts())) {
                Parts parts = new Parts(filter.getParts());
                if (parts.contains(Employee.PARTS_USER)) {
                    fetchUser(tenantId, result);
                }
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public Employee getEmployeeByUserId(String tenantId, String userId) throws BusinessException {
        try {
            PEmployee perz = employeeDao.get(PEmployee.class, "userId", userId, "tenantId", tenantId, "isDeleted", 0);
            if (perz == null) {
                return null;
            }
            Employee result = new Employee();
            BeanUtils.copyProperties(perz, result);
            result.setAdmin(perz.getAdmin());
            fetchUser(tenantId, Arrays.asList(result));
            //TODO fetchRole
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    /**
     * 获取用户信息
     */
    private void fetchUser(String tenantId, List<Employee> employeeList) {
        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }
        List<String> employeeUserIds = employeeList.parallelStream().map(Employee::getUserId).collect(Collectors.toList());
        UserFilter filter = new UserFilter();
        filter.setUserIdIn(employeeUserIds);
        List<User> userList = userService.query(tenantId, filter).getData();
        Map<String, User> userMap = new HashMap<>();
        for (User user : userList) {
            if (!userMap.containsKey(user.getUuid())) {
                userMap.put(user.getUuid(), user);
            }
        }
        for (Employee employee : employeeList) {
            employee.setUser(userMap.get(employee.getUserId()));
        }
    }

    /**
     * 添加员工参数校验
     */
    private void verification(EmployeeCreateRq createRq) {
        if (StringUtil.isNullOrBlank(createRq.getLoginId())) {
            throw new BusinessException("登录账号不可以为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getName())) {
            throw new BusinessException("员工姓名不可以为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getMobile())) {
            throw new BusinessException("手机号不可以为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getPassword())) {
            throw new BusinessException("登录密码不可以为空");
        }
    }

    /**
     * 创建管理员参数校验
     */
    private void verification(EmployeeCreateAdminRq createAdminRq) {
        if (StringUtil.isNullOrBlank(createAdminRq.getLoginId())) {
            throw new BusinessException("登录账号不可以为空");
        }
        if (StringUtil.isNullOrBlank(createAdminRq.getName())) {
            throw new BusinessException("员工姓名不可以为空");
        }
        if (StringUtil.isNullOrBlank(createAdminRq.getMobile())) {
            throw new BusinessException("手机号不可以为空");
        }
        if (StringUtil.isNullOrBlank(createAdminRq.getPassword())) {
            throw new BusinessException("登录密码不可以为空");
        }
    }

    /**
     * ID生成
     */
    private String idGen(String tenantId) {
        PEmployee maxId = employeeDao.getMaxId(tenantId);
        String prefix = "";
        int newNumber = 0;
        int idNumber = Integer.valueOf(maxId == null ? "0" : maxId.getUserId());
        if (idNumber >= 9999) {
            throw new BusinessException("员工编号已达到上限");
        }
        newNumber = idNumber + 1;
        if (String.valueOf(newNumber).length() == 1) {
            prefix += "000";
        } else if (String.valueOf(newNumber).length() == 2) {
            prefix += "00";
        } else if (String.valueOf(newNumber).length() == 3) {
            prefix += "0";
        }

        String id = prefix + newNumber;

        return id;
    }


}
