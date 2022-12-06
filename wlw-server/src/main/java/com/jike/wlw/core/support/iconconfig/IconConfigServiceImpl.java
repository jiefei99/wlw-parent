package com.jike.wlw.core.support.iconconfig;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.support.iconconfig.IconConfigDao;
import com.jike.wlw.dao.support.iconconfig.PIconConfig;
import com.jike.wlw.service.support.iconconfig.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class IconConfigServiceImpl extends BaseService implements IconConfigService {

    @Autowired
    private IconConfigDao iconConfigDao;

    @Override
    public IconConfig get(String id) throws BusinessException {
        try {
            PIconConfig perz = iconConfigDao.get(PIconConfig.class, id);
            if (perz == null) {
                return null;
            }

            IconConfig result = new IconConfig();
            BeanUtils.copyProperties(perz, result);
            result.setAppId(AppType.valueOf(perz.getAppId()));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public String create(IconConfigEditRq createRq, String operator) throws BusinessException {
        try {
            validate(createRq);

            PIconConfig perz = new PIconConfig();
            BeanUtils.copyProperties(createRq, perz);
            perz.setAppId(createRq.getAppId().name());
            perz.onCreated(operator);

            iconConfigDao.save(perz);

            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(IconConfigEditRq modifyRq, String operator) throws BusinessException {
        try {
            PIconConfig perz = iconConfigDao.get(PIconConfig.class, modifyRq.getUuid());
            if (perz == null) {
                throw new BusinessException("指定图标配置不存在或已删除");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getDescription())) {
                perz.setDescription(modifyRq.getDescription());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getUrl())) {
                perz.setUrl(modifyRq.getUrl());
            }
            perz.onModified(operator);

            iconConfigDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void delete(String id) throws BusinessException {
        try {

            iconConfigDao.remove(PIconConfig.class, id);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<IconConfig> query(IconConfigFilter filter) throws BusinessException {
        try {
            List<PIconConfig> list = iconConfigDao.query(filter);
            long total = iconConfigDao.getCount(filter);

            List<IconConfig> result = new ArrayList<>();
            for (PIconConfig perz : list) {
                IconConfig iconConfig = new IconConfig();
                BeanUtils.copyProperties(perz, iconConfig);
                iconConfig.setAppId(AppType.valueOf(perz.getAppId()));

                result.add(iconConfig);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private void validate(IconConfigEditRq result) {
        if (StringUtil.isNullOrBlank(result.getUrl())) {
            throw new BusinessException("图标不能为空");
        }
        if (StringUtil.isNullOrBlank(result.getDescription())) {
            throw new BusinessException("图标描述不能为空");
        }
        if (result.getAppId() == null) {
            throw new BusinessException("应用类型不能为空");
        }
    }
}
