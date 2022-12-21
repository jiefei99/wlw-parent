package com.jike.wlw.core.author.org.member;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jpa.api.entity.Parts;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.org.OrgDao;
import com.jike.wlw.dao.author.org.POrg;
import com.jike.wlw.dao.author.org.member.OrgMemberDao;
import com.jike.wlw.dao.author.org.member.POrgMember;
import com.jike.wlw.dao.author.user.PUser;
import com.jike.wlw.dao.author.user.UserDao;
import com.jike.wlw.service.author.org.Org;
import com.jike.wlw.service.author.org.OrgFilter;
import com.jike.wlw.service.author.org.OrgService;
import com.jike.wlw.service.author.org.OrgType;
import com.jike.wlw.service.author.org.member.OrgMember;
import com.jike.wlw.service.author.org.member.OrgMemberCreateRq;
import com.jike.wlw.service.author.org.member.OrgMemberFilter;
import com.jike.wlw.service.author.org.member.OrgMemberService;
import com.jike.wlw.service.author.user.UserCreateRq;
import com.jike.wlw.service.author.user.UserService;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Slf4j
@RestController
@ApiModel("组织成员服务实现")
@RequestMapping(value = "service/org/member", produces = "application/json;charset=utf-8")
public class OrgMemberServiceImpl implements OrgMemberService {

    @Autowired
    private OrgMemberDao orgMemberDao;
    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgDao orgDao;
    @Autowired
    private UserDao userDao;

    @Override
    public OrgMember get(String id, String parts) throws BusinessException {
        try {
            POrgMember perz = orgMemberDao.get(POrgMember.class, "orgId", id);
            if (perz == null) {
                perz = orgMemberDao.get(POrgMember.class, id);
            }
            if (perz == null) {
                return null;
            }
            OrgMember result = convert(perz);

            if (!StringUtil.isNullOrBlank(parts)) {
                Parts part = new Parts(parts);
                if (part.contains(OrgMember.PART_ORG)) {
                    fetchOrg(Arrays.asList(result));
                }
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String create(OrgMemberCreateRq createRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verificationCreateRq(createRq);
            POrg pOrg = orgDao.get(POrg.class, createRq.getOrgId());
            if (pOrg == null) {
                throw new BusinessException("当前组织不存在或已删除, 无法新增员工");
            }

            POrgMember perz = orgMemberDao.get(POrgMember.class, "orgId", createRq.getOrgId(), "mobile", createRq.getMobile());
            if (perz != null) {
                throw new BusinessException("当前用户已经是" + perz.getName() + "组织成员，无法添加");
            }

            if (Boolean.TRUE.equals(createRq.getIsAdmin())) {
                perz = orgMemberDao.get(POrgMember.class, "orgId", createRq.getOrgId(), "mobile", createRq.getMobile(), "isAdmin", 1);
                if (perz != null) {
                    throw new BusinessException("当前用户已经是" + OrgType.valueOf(perz.getOrgType()).getCaption() + "的管理员，无法设置管理员");
                }
                OrgMemberFilter orgMemberFilter = new OrgMemberFilter();
                orgMemberFilter.setIsAdmin(createRq.getIsAdmin());
                orgMemberFilter.setOrgIdEq(createRq.getOrgId());
                List<OrgMember> orgMemberList = query(orgMemberFilter).getData();
                if (!CollectionUtils.isEmpty(orgMemberList)) {
                    throw new BusinessException("当前组织已有管理员，无法新增管理员");
                }
            }
            perz = new POrgMember();
            // 新建用户
            String userId = "";
            PUser pUser = userDao.get(PUser.class, "mobile", createRq.getMobile(), "userType", UserType.MEMBER.name());
            if (pUser == null) {
                UserCreateRq userCreateRq = new UserCreateRq();
                userCreateRq.setUserType(UserType.MEMBER);
                userCreateRq.setHeadImage(createRq.getImage());
                userCreateRq.setName(createRq.getName());
                userCreateRq.setMobile(createRq.getMobile());
                userCreateRq.setSex("男");
                userId = userService.create(userCreateRq, operator);
                perz.setStatus(EnabledStatus.DISABLED.name());
            } else {
                userId = pUser.getUuid();

                if (StringUtils.isEmpty(createRq.getImage())) {
                    createRq.setImage(pUser.getHeadImage());
                }
                perz.setStatus(EnabledStatus.ENABLED.name());
            }


            BeanUtils.copyProperties(createRq, perz);
            perz.setUserId(userId);
            perz.setOrgType(pOrg.getOrgType());
            perz.setIsAdmin(createRq.getIsAdmin());
            perz.onCreated(operator);
            perz.setId(orgMemberDao.getMaxId() + 1);
            orgMemberDao.save(perz);

            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String modify(OrgMember orgMember, String operator) throws BusinessException {
        try {
            // 参数校验
            verificationModifyRq(orgMember);
            POrgMember perz = orgMemberDao.get(POrgMember.class, orgMember.getUuid());
            if (perz == null) {
                throw new BusinessException("该组织成员不存在或已删除");
            }
            List<POrgMember> saveList = new ArrayList<>();
            if (Boolean.FALSE.equals(perz.getIsAdmin()) && Boolean.TRUE.equals(orgMember.getIsAdmin())) {
                OrgMemberFilter orgMemberFilter = new OrgMemberFilter();
                orgMemberFilter.setOrgIdEq(perz.getOrgId());
                orgMemberFilter.setIsAdmin(Boolean.TRUE);
                List<POrgMember> pOrgMemberList = orgMemberDao.query(orgMemberFilter);
                if (!CollectionUtils.isEmpty(pOrgMemberList)) {
                    for (POrgMember member : pOrgMemberList) {
                        member.setIsAdmin(false);
                        saveList.add(member);
                    }
                }
            }
            BeanUtils.copyProperties(orgMember, perz, "uuid", "orgId", "userId", "mobile", "orgType");
            perz.setStatus(orgMember.getStatus().name());
            perz.onModified(operator);
            saveList.add(perz);

            orgMemberDao.save(saveList);
            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public void setAdmin(String id, String operator) throws BusinessException {
        try {
            POrgMember perz = orgMemberDao.get(POrgMember.class, "orgId", id);
            if (perz == null) {
                perz = orgMemberDao.get(POrgMember.class, id);
            }
            if (perz == null) {
                throw new BusinessException("当前成员不存在或已删除");
            }
            // 判断是否已经是该组织类型的管理员
            POrgMember orgMember = orgMemberDao.get(POrgMember.class, "orgType", perz.getOrgType(), "mobile", perz.getMobile(), "isAdmin", 1);
            if (orgMember != null) {
                throw new BusinessException("当前用户已经是" + OrgType.valueOf(orgMember.getOrgType()).getCaption() + "的管理员，无法设置管理员");
            }
            // 旧管理员设置为普通成员
            List<POrgMember> saveList = new ArrayList<>();
            OrgMemberFilter orgMemberFilter = new OrgMemberFilter();
            orgMemberFilter.setOrgIdEq(perz.getOrgId());
            orgMemberFilter.setIsAdmin(Boolean.TRUE);
            List<POrgMember> pOrgMemberList = orgMemberDao.query(orgMemberFilter);
            if (!CollectionUtils.isEmpty(pOrgMemberList)) {
                for (POrgMember pOrgMember : pOrgMemberList) {
                    pOrgMember.setIsAdmin(false);
                    saveList.add(pOrgMember);
                }
            }
            // 当前成员设置为管理员
            perz.setIsAdmin(true);
            perz.onModified(operator);
            saveList.add(perz);

            orgMemberDao.save(saveList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public void enable(String id, String operator) throws BusinessException {
        try {
            POrgMember perz = orgMemberDao.get(POrgMember.class, id);
            if (perz == null) {
                throw new BusinessException("找不到指定组织成员");
            }
            if (EnabledStatus.ENABLED.name().equals(perz.getStatus())) {
                return;
            }
            perz.setStatus(EnabledStatus.ENABLED.name());
            perz.onModified(operator);

            orgMemberDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    public void setImage(String phone, String image) throws BusinessException {

    }

    @TX
    @Override
    public void disable(String id, String operator) throws BusinessException {
        try {
            POrgMember perz = orgMemberDao.get(POrgMember.class, id);
            if (perz == null) {
                throw new BusinessException("找不到指定组织成员");
            }
            if (EnabledStatus.DISABLED.name().equals(perz.getStatus())) {
                return;
            }
            perz.setStatus(EnabledStatus.DISABLED.name());
            perz.onModified(operator);

            orgMemberDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public void delete(String id) throws BusinessException {
        try {
            POrgMember perz = orgMemberDao.get(POrgMember.class, id);
            if (perz == null) {
                throw new BusinessException("找不到指定组织成员");
            }

            orgMemberDao.remove(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public PagingResult<OrgMember> query(OrgMemberFilter filter) throws BusinessException {
        try {
//            if (filter.getStatusEq() != null || !StringUtil.isNullOrBlank(filter.getKeywords())) {
//                StoreFilter storeFilter = new StoreFilter();
//                storeFilter.setKeywords(filter.getKeywords());
//                storeFilter.setStatusEq(filter.getStatusEq());
//                List<Store> storeList = storeService.query(storeFilter).getData();
//                if (storeList.size() == 0) {
//                    return null;
//                }
//                List<String> ids = new ArrayList<>();
//                for (Store store : storeList) {
//                    ids.add(store.getOrgId());
//                }
//                filter.setOrgIdIn(ids);
//            }
            List<POrgMember> list = orgMemberDao.query(filter);
            long count = orgMemberDao.getCount(filter);

            List<OrgMember> result = new ArrayList<>();
            for (POrgMember perz : list) {
                OrgMember orgMember = convert(perz);
                result.add(orgMember);
            }

            if (!StringUtil.isNullOrBlank(filter.getParts())) {
                Parts part = new Parts(filter.getParts());
                if (part.contains(OrgMember.PART_ORG)) {
                    fetchOrg(result);
                }
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 修改组织成员请求参数校验
     */
    private void verificationModifyRq(OrgMember orgMember) {
        if (StringUtil.isNullOrBlank(orgMember.getOrgId())) {
            throw new BusinessException("组织ID不能为空");
        }
        if (StringUtil.isNullOrBlank(orgMember.getMobile())) {
            throw new BusinessException("成员手机号不能为空");
        }
        if (StringUtil.isNullOrBlank(orgMember.getName())) {
            throw new BusinessException("成员名字不能为空");
        }
        if (orgMember.getOrgType() == null) {
            throw new BusinessException("组织类型不能为空");
        }
        if (orgMember.getIsAdmin() == null) {
            throw new BusinessException("是否是管理员不能为空");
        }
    }

    /**
     * 添加成员请求参数校验
     */
    private void verificationCreateRq(OrgMemberCreateRq createRq) {
        if (StringUtil.isNullOrBlank(createRq.getOrgId())) {
            throw new BusinessException("组织ID不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getMobile())) {
            throw new BusinessException("成员手机号不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getName())) {
            throw new BusinessException("成员名字不能为空");
        }
        if (createRq.getOrgType() == null) {
            throw new BusinessException("组织类型不能为空");
        }
        if (createRq.getIsAdmin() == null) {
            throw new BusinessException("是否是管理员不能为空");
        }
    }

    /**
     * P对象转换为A对象
     */
    private OrgMember convert(POrgMember perz) {
        OrgMember orgMember = new OrgMember();
        BeanUtils.copyProperties(perz, orgMember);
        orgMember.setIsAdmin(perz.getIsAdmin());
        orgMember.setStatus(EnabledStatus.valueOf(perz.getStatus()));
        orgMember.setOrgType(OrgType.valueOf(perz.getOrgType()));
        return orgMember;
    }

    /**
     * 获取组织信息
     */
    private void fetchOrg(List<OrgMember> orgMemberList) {
        if (CollectionUtils.isEmpty(orgMemberList)) {
            return;
        }
        List<String> orgOrgIds = new ArrayList<>();
        for (OrgMember orgMember : orgMemberList) {
            orgOrgIds.add(orgMember.getOrgId());
        }
        OrgFilter filter = new OrgFilter();
        filter.setIdIn(orgOrgIds);
        List<Org> orgList = orgService.query(filter).getData();

        Map<String, Org> orgMap = new HashMap<>();
        for (Org org : orgList) {
            if (!orgMap.containsKey(org.getUuid())) {
                orgMap.put(org.getUuid(), org);
            }
        }
        for (OrgMember orgMember : orgMemberList) {
            orgMember.setOrg(orgMap.get(orgMember.getOrgId()));
        }
    }
}
