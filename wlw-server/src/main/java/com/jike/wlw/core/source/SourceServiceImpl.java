package com.jike.wlw.core.source;

import com.alibaba.druid.support.json.JSONUtils;
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
import com.jike.wlw.service.source.SourceCreateRq;
import com.jike.wlw.service.source.SourceEvns;
import com.jike.wlw.service.source.SourceFilter;
import com.jike.wlw.service.source.SourceModifyRq;
import com.jike.wlw.service.source.SourceService;
import com.jike.wlw.service.source.SourceTypes;
import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import com.jike.wlw.util.SpringUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.proton.codec.messaging.SourceType;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@ApiModel("资源服务实现")
public class SourceServiceImpl extends BaseService implements SourceService {

    @Autowired
    private SourceDao sourceDao;

    @Override
    public Source get(String tenantId, String uuid) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
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
    public void create(String tenantId, SourceCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("名称不能为空");
            }
            if (createRq.getEnvironment() == null) {
                throw new BusinessException("环境不能为空");
            }
            if (createRq.getType() == null) {
                throw new BusinessException("类型不能为空");
            }

            if (SourceEvns.CLOUD.equals(createRq.getEnvironment())) {
                if (SourceTypes.ALIYUN.equals(createRq.getType())) {
                    if (createRq.getAliyunSource() == null) {
                        throw new BusinessException("阿里云连接参数不能为空");
                    }
                    if (StringUtil.isNullOrBlank(createRq.getAliyunSource().getAccessKey()) || StringUtil.isNullOrBlank(createRq.getAliyunSource().getAccessSecret())) {
                        throw new BusinessException("阿里云连接参数缺失");
                    }
                } else {
                    throw new BusinessException("环境和资源类型不匹配");
                }
            }
            if (SourceEvns.LOCAL.equals(createRq.getEnvironment())) {
                if (SourceTypes.MQTT.equals(createRq.getType())) {
                    if (createRq.getMqttSource() == null) {
                        throw new BusinessException("mqtt连接参数不能为空");
                    }
                    if (StringUtil.isNullOrBlank(createRq.getMqttSource().getUsername()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getPassword()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getHostUrl()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getClientId()) || StringUtil.isNullOrBlank(createRq.getMqttSource().getTopic())) {
                        throw new BusinessException("mqtt连接参数缺失");
                    }
                } else if (SourceTypes.INFLUXDB.equals(createRq.getType())) {
                    if (createRq.getInfluxSource() == null) {
                        throw new BusinessException("influx连接参数不能为空");
                    }
                    if (StringUtil.isNullOrBlank(createRq.getInfluxSource().getInfluxDBUrl()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getUserName()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getPassword()) || StringUtil.isNullOrBlank(createRq.getInfluxSource().getDatabase())) {
                        throw new BusinessException("influx连接参数缺失");
                    }
                } else {
                    throw new BusinessException("环境和资源类型不匹配");
                }
            }

            PSource perz = convert(tenantId, createRq);

            SourceFilter filter = new SourceFilter();
            filter.setTenantIdEq(tenantId);
            filter.setNameEq(createRq.getName());
            List<PSource> list = sourceDao.query(filter);
            if (!CollectionUtils.isEmpty(list)) {
                if (!list.get(0).getDeleted()) {
                    throw new BusinessException("不允许存在重复名称的资源");
                } else {
                    perz.setUuid(list.get(0).getUuid());
                }
            }

            perz.onCreated(operator);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, String uuid, SourceModifyRq modifyRq, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }

            if (modifyRq.getEnvironment() != null || modifyRq.getType() != null) {
                if (modifyRq.getType() != null) {
                    if (!modifyRq.getType().name().equals(perz.getType())) {
                        switch (modifyRq.getType()) {
                            case ALIYUN:
                                if (modifyRq.getAliyunSource() == null) {
                                    throw new BusinessException("类型对应资源不存在");
                                }

                                perz.setParameter(JsonUtil.objectToJson(modifyRq.getAliyunSource()));
                                break;
                            case MQTT:
                                if (modifyRq.getMqttSource() == null) {
                                    throw new BusinessException("类型对应资源不存在");
                                }
                                perz.setParameter(JsonUtil.objectToJson(modifyRq.getMqttSource()));
                                break;
                            case INFLUXDB:
                                if (modifyRq.getInfluxSource() == null) {
                                    throw new BusinessException("类型对应资源不存在");
                                }
                                perz.setParameter(JsonUtil.objectToJson(modifyRq.getInfluxSource()));
                                break;
                            default:
                                throw new BusinessException("不支持的资源类型");
                        }
                    }
                    perz.setType(modifyRq.getType().name());
                }

                if (modifyRq.getEnvironment() != null) {
                    perz.setEnvironment(modifyRq.getEnvironment().name());
                }

                if (SourceEvns.CLOUD.name().equals(perz.getEnvironment())) {
                    if (!SourceTypes.ALIYUN.equals(perz.getType())) {
                        throw new BusinessException("环境和资源不匹配");
                    }
                }
                if (SourceEvns.LOCAL.name().equals(perz.getEnvironment())) {
                    if (!SourceTypes.MQTT.name().equals(perz.getType()) && !SourceTypes.INFLUXDB.name().equals(perz.getType())) {
                        throw new BusinessException("环境和资源不匹配");
                    }
                }
            }


            perz.onModified(operator);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String uuid, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                return;
            }
            //逻辑删除
            perz.setDeleted(true);
            perz.onModified(operator);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void connecting(String tenantId, String uuid, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }
            if (perz.getConnected()) {
                return;
            }

            perz.setConnected(true);
            perz.onModified(operator);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void disConnect(String tenantId, String uuid, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }
            if (!perz.getConnected()) {
                return;
            }

            perz.setConnected(false);
            perz.onModified(operator);
            sourceDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void checkConnecting(String tenantId, String uuid, String operator) throws BusinessException {
        try {
            Source source = get(tenantId, uuid);
            if (SourceEvns.CLOUD.equals(source.getEnvironment()) && SourceTypes.ALIYUN.equals(source.getType())) {
                if (source.getAliyunSource() == null) {
                    throw new BusinessException("参数丢失");
                }
                connectToNewAliyun(source.getAliyunSource(), tenantId, uuid, operator);
            } else if (SourceEvns.LOCAL.equals(source.getEnvironment())) {
                if (SourceTypes.MQTT.equals(source.getType())) {
                    if (source.getMqttSource() == null) {
                        throw new BusinessException("参数丢失");
                    }
                    connectToNewMqtt(source.getMqttSource(), tenantId, uuid, operator);
                } else if (SourceTypes.INFLUXDB.equals(source.getType())) {
                    if (source.getInfluxSource() == null) {
                        throw new BusinessException("参数丢失");
                    }
                    connectToNewInfluxDB(source.getInfluxSource(), tenantId, uuid, operator);
                }
            } else {
                throw new BusinessException("不支持的资源环境或类型");
            }
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
        if (SourceTypes.ALIYUN.equals(perz.getType())) {
            result.setAliyunSource(JsonUtil.jsonToObject(perz.getParameter(), AliyunSource.class));
        }
        if (SourceTypes.MQTT.equals(perz.getType())) {
            result.setMqttSource(JsonUtil.jsonToObject(perz.getParameter(), MqttSource.class));
        }
        if (SourceTypes.INFLUXDB.equals(perz.getType())) {
            result.setInfluxSource(JsonUtil.jsonToObject(perz.getParameter(), InfluxSource.class));
        }

        return result;
    }

    private PSource convert(String tenantId, SourceCreateRq source) throws Exception {
        if (source == null) {
            return null;
        }

        PSource perz = new PSource();
        perz.setName(source.getName());
        perz.setEnvironment(source.getEnvironment().name());
        perz.setType(source.getType().name());
        perz.setTenantId(tenantId);
        if (SourceTypes.ALIYUN.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getAliyunSource()));
        }
        if (SourceTypes.MQTT.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getMqttSource()));
        }
        if (SourceTypes.INFLUXDB.equals(source.getType())) {
            perz.setParameter(JsonUtil.objectToJson(source.getInfluxSource()));
        }

        return perz;
    }


    private void connectToNewAliyun(AliyunSource aliyunSource, String tenantId, String targetId, String operator) throws Exception {
        if (aliyunSource == null) {
            throw new BusinessException("阿里云资源连接参数不能为空");
        }
        if (StringUtil.isNullOrBlank(aliyunSource.getAccessKey())) {
            throw new BusinessException("阿里云资源连接参数AccessKey不能为空");
        }
        if (StringUtil.isNullOrBlank(aliyunSource.getAccessSecret())) {
            throw new BusinessException("阿里云资源连接参数AccessSecret不能为空");
        }

        AliIotClientByConfig config = SpringUtil.getBean(AliIotClientByConfig.class);
        config.setAccessKeyId(aliyunSource.getAccessKey());
        config.setAccessKeySecret(aliyunSource.getAccessSecret());
        AliIotClient client = new AliIotClient(config);
        QueryProductListRequest request = new QueryProductListRequest();
        request.setCurrentPage(1);
        request.setPageSize(10);
        QueryProductListResponse response = client.queryProductList(request);
        if (response != null && response.getBody() != null && Boolean.TRUE.equals(response.getBody().getSuccess())) {
            changeConnecting(client, "aliIotClient", tenantId, targetId, operator);
        } else {
            throw new BusinessException("连接失败！失败原因：" + response.getBody().getErrorMessage());
        }
    }

    private void connectToNewMqtt(MqttSource mqttSource, String tenantId, String targetId, String operator) throws Exception {//// TODO: 2023/2/10
        EmqxClient emqxClient = new EmqxClient();
        emqxClient.connect(mqttSource.getHostUrl(), mqttSource.getClientId(), mqttSource.getUsername(), mqttSource.getPassword(), mqttSource.getTimeout(), mqttSource.getKeepalive(), mqttSource.getTopic());
        if (emqxClient.isConnected()) {
            emqxClient.disconnect(null);
            changeConnecting(emqxClient, "emqxClient", tenantId, targetId, operator);
        } else {
            throw new BusinessException("连接失败！");
        }
    }


    private void connectToNewInfluxDB(InfluxSource influxSource, String tenantId, String targetId, String operator) throws Exception {
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
            changeConnecting(influxDB, "influxDB", tenantId, targetId, operator);
        } else {
            throw new BusinessException("连接失败！");
        }
    }

    private String getConnecting(String tenantId) {
        SourceFilter filter = new SourceFilter();
        filter.setTenantIdEq(tenantId);
        filter.setConnectedEq(true);
        filter.setDeletedEq(false);
        List<PSource> list = sourceDao.query(filter);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0).getUuid();
    }

    private void changeConnecting(Object targetObject, String beanName, String tenantId, String id, String operator) throws Exception {
        String oldUuid = getConnecting(tenantId);
        disConnect(tenantId, oldUuid, operator);
        SpringUtil.replaceBean(beanName, targetObject);
        connecting(tenantId, id, operator);
    }
}
