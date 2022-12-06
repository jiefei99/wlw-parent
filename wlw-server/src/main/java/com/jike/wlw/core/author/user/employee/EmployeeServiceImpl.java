package com.jike.wlw.core.author.user.employee;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.MD5Util;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jpa.api.entity.Parts;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.user.PUser;
import com.jike.wlw.dao.author.user.UserDao;
import com.jike.wlw.dao.author.user.credentials.account.pwd.PPwdAccount;
import com.jike.wlw.dao.author.user.credentials.account.pwd.PwdAccountDao;
import com.jike.wlw.dao.author.user.employee.EmployeeDao;
import com.jike.wlw.dao.author.user.employee.PEmployee;
import com.jike.wlw.service.author.user.User;
import com.jike.wlw.service.author.user.UserCreateRq;
import com.jike.wlw.service.author.user.UserFilter;
import com.jike.wlw.service.author.user.UserModifyRq;
import com.jike.wlw.service.author.user.UserService;
import com.jike.wlw.service.author.user.UserType;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccount;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountFilter;
import com.jike.wlw.service.author.user.employee.Employee;
import com.jike.wlw.service.author.user.employee.EmployeeCreateAdminRq;
import com.jike.wlw.service.author.user.employee.EmployeeCreateRq;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import com.jike.wlw.service.author.user.employee.EmployeeModifyRq;
import com.jike.wlw.service.author.user.employee.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengchen
 * @date 2022/7/19
 * @apiNote
 */
@Slf4j
@RestController
public class EmployeeServiceImpl extends BaseService implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PwdAccountDao pwdAccountDao;

    @Override
    public Employee get(String id) throws BusinessException {
        try {
            PEmployee perz = employeeDao.get(PEmployee.class, "userId", id);
            if (perz == null) {
                perz = employeeDao.get(PEmployee.class, id);
            }
            if (perz == null) {
                return null;
            }
            Employee result = new Employee();
            BeanUtils.copyProperties(perz, result);
            result.setAdmin(perz.getAdmin());

            fetchUser(Arrays.asList(result));
            fetchPwdAccount(Arrays.asList(result));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String create(EmployeeCreateRq createRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verification(createRq);

            PEmployee perz = new PEmployee();
            PUser pUser = userDao.get(PUser.class, "mobile", createRq.getMobile());
            if (pUser != null) {
                return pUser.getUuid();
            }
            UserCreateRq userCreateRq = new UserCreateRq();
            userCreateRq.setUserType(UserType.EMPLOYEE);
            userCreateRq.setMobile(createRq.getMobile());
            userCreateRq.setName(createRq.getName());
            userCreateRq.setSex(createRq.getSex());
            String userId = userService.create(userCreateRq, operator);
            perz.setId(idGen(createRq.getOrgType().name(), createRq.getOrgId()));
            while(perz.getId() == null){
                perz.setId(idGen(createRq.getOrgType().name(), createRq.getOrgId()));
            }
            perz.setAdmin(false);
            perz.onCreated(operator);
            perz.setUserId(userId);
            perz.setOrgId(createRq.getOrgId());
            perz.setOrgType(createRq.getOrgType().name());
            employeeDao.save(perz);

            PPwdAccount pwdAccount = new PPwdAccount();
            PwdAccountFilter filter = new PwdAccountFilter();
            List<PPwdAccount> pPwdAccountList = new ArrayList<>();
            filter.setLoginIdEq(createRq.getLoginId());
            pPwdAccountList = pwdAccountDao.query(filter);
            if(!CollectionUtils.isEmpty(pPwdAccountList)){
                throw new BusinessException("当前登录账号已经存在，无法新建");
            }
            pwdAccount.setLoginId(createRq.getLoginId());
            pwdAccount.setUserType(UserType.EMPLOYEE.name());
            pwdAccount.setPassword(MD5Util.getMD5String(createRq.getPassword()));
            pwdAccount.setUserId(userId);
            pwdAccountDao.save(pwdAccount);

            // TODO 需创建“员工-角色关联关系”，等角色接口提供完成后实现

            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String createAdmin(EmployeeCreateAdminRq adminRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verification(adminRq);

            PEmployee perz = employeeDao.get(PEmployee.class, "orgType", adminRq.getOrgType().name(), "orgId", adminRq.getOrgId(), "admin", "1");
            if (perz != null) {
                return perz.getUserId();
            }
            UserCreateRq createRq = new UserCreateRq();
            createRq.setUserType(UserType.EMPLOYEE);
            createRq.setName(adminRq.getName());
            createRq.setMobile(adminRq.getMobile());
            String userId = userService.create(createRq, operator);

            perz = new PEmployee();
            perz.onCreated(operator);
            perz.setAdmin(true);
            perz.setUserId(userId);
            perz.setOrgType(adminRq.getOrgType().name());
            perz.setOrgId(adminRq.getOrgId());
            perz.setId(idGen(adminRq.getOrgType().name(), adminRq.getOrgId()));
            employeeDao.save(perz);

            PPwdAccount pwdAccount = new PPwdAccount();
            pwdAccount.setLoginId(adminRq.getLoginId());
            pwdAccount.setPassword(MD5Util.getMD5String(adminRq.getPassword()));
            pwdAccount.setUserType(UserType.EMPLOYEE.name());
            pwdAccount.setUserId(userId);
            pwdAccountDao.save(pwdAccount);

            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public void modify(EmployeeModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(modifyRq.getUserId())) {
                throw new BusinessException("员工的用户id不可为空");
            }

            UserModifyRq userModifyRq = new UserModifyRq();
            userModifyRq.setUuid(modifyRq.getUserId());
            userModifyRq.setName(modifyRq.getName());
            userModifyRq.setMobile(modifyRq.getMobile());
            userModifyRq.setSex(modifyRq.getSex());
            userModifyRq.setRemark(modifyRq.getRemark());
            userService.modify(userModifyRq, operator);

            //TODO  修改“用户-角色关联关系”  等角色接口实现之后完成

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public PagingResult<Employee> query(EmployeeFilter filter) throws BusinessException {
        try {
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
                    fetchUser(result);
                }
                if (parts.contains(Employee.PARTS_PWD_ACCOUNT)) {
                    fetchPwdAccount(result);
                }
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    /**
     * 获取用户信息
     */
    private void fetchUser(List<Employee> employeeList) {
        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }
        List<String> employeeUserIds = new ArrayList<>();
        for (Employee employee : employeeList) {
            employeeUserIds.add(employee.getUserId());
        }
        UserFilter filter = new UserFilter();
        filter.setUserIdIn(employeeUserIds);
        List<User> userList = userService.query(filter).getData();

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
     * 获取密码账户信息
     */
    private void fetchPwdAccount(List<Employee> employeeList) {
        if (CollectionUtils.isEmpty(employeeList)) {
            return;
        }

        List<String> employeeUserIds = new ArrayList<>();
        for (Employee employee : employeeList) {
            employeeUserIds.add(employee.getUserId());
        }
        PwdAccountFilter filter = new PwdAccountFilter();
        filter.setUserIdIn(employeeUserIds);
        List<PPwdAccount> pPwdAccountList = pwdAccountDao.query(filter);
        List<PwdAccount> pwdAccountList = new ArrayList<>();
        for (PPwdAccount pPwdAccount : pPwdAccountList) {
            PwdAccount pwdAccount = new PwdAccount();
            BeanUtils.copyProperties(pPwdAccount, pwdAccount);
            pwdAccount.setUserType(UserType.valueOf(pPwdAccount.getUserType()));
            pwdAccountList.add(pwdAccount);
        }
        Map<String, PwdAccount> pwdAccountMap = new HashMap<>();
        for (PwdAccount pwdAccount : pwdAccountList) {
            if (!pwdAccountMap.containsKey(pwdAccount.getUserId())) {
                pwdAccountMap.put(pwdAccount.getUserId(), pwdAccount);
            }
        }

        for (Employee employee : employeeList) {
            if (pwdAccountMap.containsKey(employee.getUserId())) {
                employee.setLoginId(pwdAccountMap.get(employee.getUserId()).getLoginId());
                employee.setPassword(pwdAccountMap.get(employee.getUserId()).getPassword());
            }

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
        if (StringUtil.isNullOrBlank(createRq.getSex())) {
            throw new BusinessException("性别不可以为空");
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
    private String idGen(String orgType, String orgId) {
        PEmployee maxId = employeeDao.getMaxId(orgType, orgId);
        String prefix = "";
        int newNumber = 0;
        int idNumber = Integer.valueOf(maxId == null ? "0" : maxId.getId());
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
