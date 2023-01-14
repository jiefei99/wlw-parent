package com.jike.wlw.core.author.tenant;

import com.geeker123.rumba.commons.base.EnabledStatus;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.commons.util.converter.ConverterUtil;
import com.geeker123.rumba.jpa.api.entity.IdName;
import com.geeker123.rumba.jpa.api.entity.IdNameList;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.author.tenant.PTenant;
import com.jike.wlw.dao.author.tenant.TenantDao;
import com.jike.wlw.service.author.tenant.Tenant;
import com.jike.wlw.service.author.tenant.TenantFilter;
import com.jike.wlw.service.author.tenant.TenantService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史： 2020/3/11 11:45- sufengjia - 创建。
 */
@Slf4j
@RestController
@ApiModel("租户服务实现")
public class TenantServiceImpl extends BaseService implements TenantService {

    @Autowired
    private TenantDao tenantDao;

    @Override
    public Tenant get(String id) throws BusinessException {
        try {
            PTenant perz = tenantDao.get(PTenant.class, id);
            if (perz == null) {
                perz = tenantDao.get(PTenant.class, "code", id);
            }
            if (perz == null) {
                return null;
            }

            Tenant result = new Tenant();
            BeanUtils.copyProperties(perz, result);

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String save(Tenant tenant, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(tenant.getName())) {
                throw new BusinessException("租户名称不能为空");
            }

            TenantFilter filter = new TenantFilter();
            filter.setStatus(EnabledStatus.ENABLED);
            filter.setName(tenant.getName());
            List<PTenant> tenantList = tenantDao.query(filter);
            if (!CollectionUtils.isEmpty(tenantList)) {
                throw new BusinessException("该名称的租户已存在");
            }

            PTenant perz = null;
            if (StringUtil.isNullOrBlank(tenant.getUuid())) {
                perz = new PTenant();
                perz.setId(tenant.getId());
                perz.onCreated(operator);
            } else {
                perz = tenantDao.get(PTenant.class, tenant.getUuid());
                if (perz == null) {
                    throw new BusinessException("指定租户不存在或已删除");
                }
                perz.onModified(operator);
            }

            perz.setName(tenant.getName());
            tenantDao.save(perz);
            return perz.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void enable(String id, String operator) throws BusinessException {
        try {
            PTenant perz = tenantDao.get(PTenant.class, id);
            if (perz == null) {
                perz = tenantDao.get(PTenant.class, "code", id);
            }
            if (perz == null) {
                throw new BusinessException("指定的租户不存在或已删除");
            }
            if (EnabledStatus.ENABLED.name().equals(perz.getStatus())) {
                return;
            }

            perz.setStatus(EnabledStatus.ENABLED.name());
            perz.onModified(operator);
            tenantDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void disable(String id, String operator) throws BusinessException {
        try {
            PTenant perz = tenantDao.get(PTenant.class, id);
            if (perz == null) {
                perz = tenantDao.get(PTenant.class, "code", id);
            }
            if (perz == null) {
                throw new BusinessException("租户不存在或已被删除");
            }

            if (EnabledStatus.DISABLED.name().equals(perz.getStatus())) {
                return;
            }

            perz.setStatus(EnabledStatus.DISABLED.name());
            perz.onModified(operator);
            tenantDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }


    }

    @Override
    public PagingResult<Tenant> query(TenantFilter filter) throws BusinessException {
        try {
            List<PTenant> list = tenantDao.query(filter);
            long total = tenantDao.getCount(filter);
            try {
                PagingResult<Tenant> result = new PagingResult<>(filter.getPage(), filter.getPageSize(), total, ConverterUtil.converts(list, Tenant.class));

                return result;
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new BusinessException(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void fetchName(IdNameList idNameList) throws BusinessException {
        if (idNameList.getItems().isEmpty()) {
            return;
        }
        List<String> ids = new ArrayList<String>();
        for (IdName item : idNameList.getItems()) {
            ids.add(item.getId());
        }
        TenantFilter filter = new TenantFilter();
        filter.setIds(ids);
        List<PTenant> list = tenantDao.query(filter);
        if (list.isEmpty()) {
            return;
        }
        Map<String, String> idNameMapper = new HashMap<String, String>();
        for (PTenant tenant : list) {
            idNameMapper.put(tenant.getId(), tenant.getName());
        }
        for (IdName item : idNameList.getItems()) {
            item.setName(idNameMapper.get(item.getId()));
        }
    }
}
