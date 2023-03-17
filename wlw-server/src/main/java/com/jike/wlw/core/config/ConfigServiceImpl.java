package com.jike.wlw.core.config;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.commons.util.converter.ConverterUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.config.ConfigDao;
import com.jike.wlw.dao.config.PConfig;
import com.jike.wlw.service.config.Config;
import com.jike.wlw.service.config.ConfigGroup;
import com.jike.wlw.service.config.ConfigService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@ApiModel("配置服务实现")
@RequestMapping(value = "service/config", produces = "application/json;charset=utf-8")
public class ConfigServiceImpl extends BaseService implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public Config get(String group, String key) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(group, "group");
            Assert.assertArgumentNotNull(key, "key");

            Config config = new Config();
            PConfig perz = configDao.get(PConfig.class, "configGroup", group, "configKey", key);
            if (perz == null) {
                return null;
            }
            BeanUtils.copyProperties(perz, config);

            return config;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ConfigGroup getGroup(String group) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(group, "group");

            List<PConfig> list = configDao.getList(PConfig.class, "configGroup", group);

            ConfigGroup result = new ConfigGroup();
            result.setConfigs(ConverterUtil.converts(list, Config.class));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public String save(Config config, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(config, "config");

            if (StringUtil.isNullOrBlank(config.getConfigGroup())) {
                throw new BusinessException("配置组不能为空");
            }
            if (StringUtil.isNullOrBlank(config.getConfigKey())) {
                throw new BusinessException("配置组下的key不能为空");
            }
//            if (StringUtil.isNullOrBlank(config.getConfigValue())) {
//                throw new BusinessException(config.getConfigName() + "配置对应的值不能为空");
//            }

            PConfig perz = null;
            if (StringUtil.isNullOrBlank(config.getUuid())) {
                perz = new PConfig();
                perz.onCreated(operator);
            } else {
                perz = configDao.get(PConfig.class, config.getUuid());
                if (perz == null) {
                    throw new BusinessException("配置不存在或已被删除");
                }
            }

            BeanUtils.copyProperties(config, perz, "creator", "created", "modifier", "modified");
            perz.onModified(operator);
            if (StringUtils.isEmpty(perz.getTenant())) {
                Config configTenant = new Config();
                perz.setTenant(configTenant.getTenant());
            }
            configDao.save(perz);

            return perz.getUuid();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void saveGroup(ConfigGroup group, String operator) throws BusinessException {
        Assert.assertArgumentNotNull(group, "group");

        for (Config config : group.getConfigs()) {
            save(config, operator);
        }
    }

    @TX
    @Override
    public void deleteGroup(String group) throws BusinessException {

        Assert.assertArgumentNotNull(group, "group");

        configDao.deleteByGroup(group);
    }
}
