package com.jike.wlw.core.author.org;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.org.OrgDao;
import com.jike.wlw.dao.author.org.POrg;
import com.jike.wlw.service.author.org.Org;
import com.jike.wlw.service.author.org.OrgCreateRq;
import com.jike.wlw.service.author.org.OrgFilter;
import com.jike.wlw.service.author.org.OrgService;
import com.jike.wlw.service.author.org.OrgType;
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
 * @date 2022/7/20
 * @apiNote
 */
@Slf4j
@ApiModel("组织服务实现")
@RestController
@RequestMapping(value = "service/org", produces = "application/json;charset=utf-8")
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgDao orgDao;

    @Override
    public Org get(String id) throws BusinessException {
        try {
            POrg perz = orgDao.get(POrg.class, id);
            if (perz == null) {
                return null;
            }
            Org result = convert(perz);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String create(OrgCreateRq orgCreateRq, String operator) throws BusinessException {
        try {
            // 参数校验
            verificationCreateRq(orgCreateRq);

            POrg perz = orgDao.get(POrg.class, "name", orgCreateRq.getName());
            if (perz != null) {
                throw new BusinessException("组织“" + orgCreateRq.getName() + "”已存在，无法重复创建");
            }

            perz = new POrg();
            BeanUtils.copyProperties(orgCreateRq, perz);
            perz.setOrgType(orgCreateRq.getOrgType().name());
            perz.onCreated(operator);
            perz.setId(orgDao.getMaxId() + 1);
            orgDao.save(perz);
            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @TX
    @Override
    public String modify(Org org, String operator) throws BusinessException {
        try {
            // 参数校验
            if (StringUtil.isNullOrBlank(org.getUuid())) {
                throw new BusinessException("组织ID不能为空");
            }
            POrg perz = orgDao.get(POrg.class, org.getUuid());
            if (perz == null) {
                throw new BusinessException("该组织不存在或已被删除");
            }
            BeanUtils.copyProperties(org, perz, "id", "orgType", "uuid", "upperId");
            perz.setOrgType(org.getOrgType().name());

            orgDao.save(perz);
            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public PagingResult<Org> query(OrgFilter filter) throws BusinessException {
        try {
            List<POrg> list = orgDao.query(filter);
            long count = orgDao.getCount(filter);

            List<Org> result = new ArrayList<>();
            for (POrg perz : list) {
                Org org = convert(perz);
                result.add(org);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 将P对象转换为A对象
     */
    private Org convert(POrg perz) {
        Org result = new Org();
        BeanUtils.copyProperties(perz, result);
        result.setOrgType(OrgType.valueOf(perz.getOrgType()));

        return result;
    }

    /**
     * 创建组织请求参数校验
     */
    private void verificationCreateRq(OrgCreateRq orgCreateRq) {
        if (orgCreateRq.getOrgType() == null) {
            throw new BusinessException("组织类型不能为空");
        }
        if (StringUtil.isNullOrBlank(orgCreateRq.getName())) {
            throw new BusinessException("名称不能为空");
        }
    }
}
