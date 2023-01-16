package com.jike.wlw.core.source;

import com.aliyun.iot20180120.models.QueryProductListRequest;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.aliyun.iot20180120.models.QueryProductRequest;
import com.aliyun.iot20180120.models.QueryProductResponse;
import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.config.client.AliIotClientByConfig;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.support.emqx.EmqxClient;
import com.jike.wlw.dao.InfluxDao;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.source.PSource;
import com.jike.wlw.dao.source.SourceDao;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceFilter;
import com.jike.wlw.service.source.SourceService;
import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import com.jike.wlw.util.SpringUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@ApiModel("资源服务实现")
public class SourceServiceImpl extends BaseService implements SourceService {

    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private SpringUtil springUtil;
    @Autowired
    private InfluxDao influxDao;

    @Override
    public Source get(String tenantId, String uuid) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId);
            if (perz == null || (perz.getDeleted() != null && perz.getDeleted())) {
                return null;
            }

            return convert(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void create(String tenantId, Source createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("名称不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getEnvironment())) {
                throw new BusinessException("环境不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getType())) {
                throw new BusinessException("类型不能为空");
            } else {
                if (Source.TYPE_ALIYUN.equals(createRq.getType())) {
                    if (createRq.getAliyunSource() == null) {
                        throw new BusinessException("阿里云连接参数不能为空");
                    } else if (StringUtil.isNullOrBlank(createRq.getAliyunSource().getAccessKey()) || StringUtil.isNullOrBlank(createRq.getAliyunSource().getAccessSecret())) {
                        throw new BusinessException("阿里云连接参数缺失");
                    }
                } else if (Source.TYPE_MQTT.equals(createRq.getType()) && createRq.getMqttSource() == null) {
                    if (createRq.getMqttSource() == null) {
                        throw new BusinessException("mqtt连接参数不能为空");
                    } else if (StringUtil.isNullOrBlank(createRq.getMqttSource().getUsername()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getPassword()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getHostUrl()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getClientId()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getTopic())) {
                        throw new BusinessException("mqtt连接参数缺失");
                    }
                } else if (Source.TYPE_INFLUX.equals(createRq.getType()) && createRq.getInfluxSource() == null) {
                    if (createRq.getInfluxSource() == null) {
                        throw new BusinessException("influx连接参数不能为空");
                    } else if (StringUtil.isNullOrBlank(createRq.getInfluxSource().getInfluxDBUrl()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getUserName()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getPassword()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getDatabase())) {
                        throw new BusinessException("influx连接参数缺失");
                    }
                }
            }

            SourceFilter filter = new SourceFilter();
            filter.setTenantIdEq(tenantId);
            filter.setNameEq(createRq.getName());
            List<PSource> list = sourceDao.query(filter);
            if (!CollectionUtils.isEmpty(list)) {
                if (list.get(0).getDeleted() != null && !list.get(0).getDeleted()) {
                    throw new BusinessException("同租户不允许存在重复名称的资源");
                } else {
                    createRq.setUuid(list.get(0).getUuid());
                }
            }

            sourceDao.save(convert(tenantId, createRq));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, String uuid, Source modifyRq, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId);
            if (perz == null || (perz.getDeleted() != null && perz.getDeleted())) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (modifyRq.getDeleted() != null) {
                perz.setDeleted(modifyRq.getDeleted());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getEnvironment())) {
                perz.setEnvironment(modifyRq.getEnvironment());
                if (Source.EVN_CLOUD.equals(modifyRq.getEnvironment()) && !StringUtil.isNullOrBlank(modifyRq.getType())) {
                    perz.setType(modifyRq.getType());
                    if (Source.TYPE_ALIYUN.equals(modifyRq.getType()) && modifyRq.getAliyunSource() != null) {
                        perz.setParameter(JsonUtil.objectToJson(modifyRq.getAliyunSource()));
                    }
                } else if (Source.EVN_LOCAL.equals(modifyRq.getEnvironment()) && !StringUtil.isNullOrBlank(modifyRq.getType())) {
                    perz.setType(modifyRq.getType());
                    if (Source.TYPE_MQTT.equals(modifyRq.getType()) && modifyRq.getMqttSource() != null) {
                        perz.setParameter(JsonUtil.objectToJson(modifyRq.getMqttSource()));
                    } else if (Source.TYPE_INFLUX.equals(modifyRq.getType()) && modifyRq.getInfluxSource() != null) {
                        perz.setParameter(JsonUtil.objectToJson(modifyRq.getInfluxSource()));
                    }
                }
            }

            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String uuid) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId);
            if (perz == null || (perz.getDeleted() != null && perz.getDeleted())) {
                return;
            }
            //逻辑删除
            perz.setDeleted(true);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void connecting(String tenantId, String uuid) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId);
            if (perz == null || (perz.getDeleted() != null && perz.getDeleted())) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }
            if (perz.getConnected()) {
                return;
            }

            SourceFilter filter = new SourceFilter();
            filter.setTenantIdEq(tenantId);
            filter.setConnectedEq(true);
            List<PSource> list = sourceDao.query(filter);
            if (!CollectionUtils.isEmpty(list)) {
                for (PSource pSource : list) {
                    pSource.setConnected(false);
                    sourceDao.save(pSource);
                }
            }

            perz.setConnected(true);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void disConnect(String tenantId, String uuid) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId);
            if (perz == null || (perz.getDeleted() != null && perz.getDeleted())) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }
            if (perz.getConnected() && !perz.getConnected()) {
                return;
            }

            perz.setConnected(false);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult checkConnecting(String tenantId, String uuid) throws BusinessException {
        try {
            Source source = get(tenantId, uuid);
            if (Source.EVN_CLOUD.equals(source.getEnvironment()) && Source.TYPE_ALIYUN.equals(source.getType()) && source.getAliyunSource() != null) {
                try {
                    connectToNewAliyun(source.getAliyunSource());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return ActionResult.fail("阿里云资源连接失败：" + e.getMessage());
                }
                return ActionResult.ok();
            } else if (Source.EVN_LOCAL.equals(source.getEnvironment())) {
                if (Source.TYPE_MQTT.equals(source.getType()) && source.getMqttSource() != null) {
                    try {
                        connectToNewMqtt(source.getMqttSource());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return ActionResult.fail("mqtt资源连接失败：" + e.getMessage());
                    }
                    return ActionResult.ok();
                } else if (Source.TYPE_INFLUX.equals(source.getType()) && source.getInfluxSource() != null) {
                    try {
                        connectToNewInfluxDB(source.getInfluxSource());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return ActionResult.fail("influxDB资源连接失败：" + e.getMessage());
                    }
                    return ActionResult.ok();
                }
            }

            return ActionResult.fail("连接资源失败：环境：" + source.getEnvironment() + "，类型" + source.getType());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Source> query(String tenantId, SourceFilter filter) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PSource> list = sourceDao.query(filter);
            long count = sourceDao.getCount(filter);

            List<Source> result = new ArrayList<>();
            for (PSource perz : list) {
                result.add(convert(perz));
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private Source convert(PSource perz) throws Exception {
        if (perz == null) {
            return null;
        }

        Source result = new Source();
        BeanUtils.copyProperties(perz, result);
        if (Source.TYPE_ALIYUN.equals(perz.getType())) {
            result.setAliyunSource(JsonUtil.jsonToObject(perz.getParameter(), AliyunSource.class));
        }
        if (Source.TYPE_MQTT.equals(perz.getType())) {
            result.setMqttSource(JsonUtil.jsonToObject(perz.getParameter(), MqttSource.class));
        }
        if (Source.TYPE_INFLUX.equals(perz.getType())) {
            result.setInfluxSource(JsonUtil.jsonToObject(perz.getParameter(), InfluxSource.class));
        }

        return result;
    }

    private PSource convert(String tenantId, Source source) throws Exception {
        if (source == null) {
            return null;
        }

        PSource perz = new PSource();
        BeanUtils.copyProperties(source, perz);
        perz.setTenantId(tenantId);
        if (Source.TYPE_ALIYUN.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getAliyunSource()));
        }
        if (Source.TYPE_MQTT.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getMqttSource()));
        }
        if (Source.TYPE_INFLUX.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getInfluxSource()));
        }

        return perz;
    }


    private void connectToNewAliyun(AliyunSource aliyunSource) throws Exception {
        AliIotClientByConfig config = SpringUtil.getBean(AliIotClientByConfig.class);
        config.setAccessKeyId(aliyunSource.getAccessKey());
        config.setAccessKeySecret(aliyunSource.getAccessSecret());
        AliIotClient client = new AliIotClient(config);
        QueryProductListRequest request = new QueryProductListRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        QueryProductListResponse response = client.queryProductList(request);
        if (response != null && response.getBody() != null && Boolean.TRUE.equals(response.getBody().getSuccess())) {
            SpringUtil.replaceBean("aliIotClient", client);
        } else {
            throw new BusinessException("连接失败！");
        }
    }

    private void connectToNewMqtt(MqttSource mqttSource) throws Exception {
        EmqxClient emqxClient = new EmqxClient();
        emqxClient.connect(mqttSource.getHostUrl(), mqttSource.getClientId(), mqttSource.getUsername(), mqttSource.getPassword(), mqttSource.getTimeout(), mqttSource.getKeepalive(), mqttSource.getTopic());
        if (emqxClient.isConnected()) {
            emqxClient.disconnect(null);
            SpringUtil.replaceBean("emqxClient", emqxClient);
        } else {
            throw new BusinessException("连接失败！");
        }
    }


    private void connectToNewInfluxDB(InfluxSource influxSource) throws Exception {
        InfluxDB influxDB = InfluxDBFactory.connect(influxSource.getInfluxDBUrl(), influxSource.getUserName(), influxSource.getPassword());
        influxDB.setDatabase(influxSource.getDatabase()).enableBatch(100, 2000, TimeUnit.MILLISECONDS);
        if (influxSource.getRetentionPolicy() != null) {
            influxDB.setRetentionPolicy(influxSource.getRetentionPolicy());
        } else {
            influxDB.setRetentionPolicy("autogen");
        }
        boolean hasLogLevel = false;
        if (influxSource.getLogLevel() != null) {
            for (InfluxDB.LogLevel logLevel : InfluxDB.LogLevel.values()) {
                if (logLevel.name().equals(influxSource.getLogLevel())) {
                    hasLogLevel = true;
                    influxDB.setLogLevel(logLevel);
                }
            }
        }
        if (!hasLogLevel) {
            influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        }

        if (influxDB.databaseExists(influxSource.getDatabase())) {
            influxDB.close();
            springUtil.replaceBean("influxDB", influxDB);
        } else {
            throw new BusinessException("连接失败！");
        }
    }

}
