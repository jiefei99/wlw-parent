package com.jike.wlw.core.source;

import com.aliyun.iot20180120.models.QueryProductListRequest;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.jike.wlw.config.client.AliIotClient;
import com.jike.wlw.config.client.AliIotClientByConfig;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.support.emqx.EmqxClient;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.source.PSource;
import com.jike.wlw.dao.source.SourceDao;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceEvns;
import com.jike.wlw.service.source.SourceFilter;
import com.jike.wlw.service.source.SourceInfo;
import com.jike.wlw.service.source.SourceSaveRq;
import com.jike.wlw.service.source.SourceService;
import com.jike.wlw.service.source.SourceStatus;
import com.jike.wlw.service.source.SourceTypes;
import com.jike.wlw.service.source.iot.AliyunSource;
import com.jike.wlw.service.source.local.InfluxSource;
import com.jike.wlw.service.source.local.MqttSource;
import com.jike.wlw.util.SpringUtil;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public void create(String tenantId, SourceSaveRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(createRq.getName())) {
                throw new BusinessException("名称不能为空");
            }
            //校验环境、类型、连接参数
            checkRelationParam(createRq.getEnvironment(), createRq.getType(), createRq.getSourceInfo());

            PSource perz = convert(tenantId, createRq);

            //存在重名资源，如果已删除则恢复，否则报错
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
    public void modify(String tenantId, String uuid, SourceSaveRq modifyRq, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }

            //校验环境、类型、连接参数
            SourceEvns evn;
            SourceTypes type;
            SourceInfo info;
            if (modifyRq.getEnvironment() != null) {
                evn = modifyRq.getEnvironment();
            } else {
                evn = SourceEvns.valueOf(perz.getEnvironment());
            }
            if (modifyRq.getType() != null) {
                type = modifyRq.getType();
            } else {
                type = SourceTypes.valueOf(perz.getType());
            }
            if (modifyRq.getSourceInfo() != null) {
                info = modifyRq.getSourceInfo();
            } else {
                info = convertSourceInfo(type, perz.getParameter());
            }
            checkRelationParam(evn, type, info);

            //校验通过，直接赋值
            if (StringUtils.isNotBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (modifyRq.getEnvironment() != null) {
                perz.setEnvironment(modifyRq.getEnvironment().name());
            }
            if (modifyRq.getType() != null) {
                perz.setType(modifyRq.getType().name());
            }
            if (modifyRq.getSourceInfo() != null) {
                perz.setParameter(convertSourceInfo(modifyRq.getType(), modifyRq.getSourceInfo()));
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
    public void disConnect(String tenantId, String uuid, String operator) throws BusinessException {
        try {
            PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
            if (perz == null) {
                throw new BusinessException("指定资源信息不存在或已删除，无法编辑");
            }
            if (SourceStatus.DISCONNECT.name().equals(perz.getStatus())) {
                return;
            }

            perz.setStatus(SourceStatus.DISCONNECT.name());
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
            Source source = get(tenantId, uuid);
            if (source == null) {
                throw new BusinessException("指定资源信息不存在或已删除");
            }
            if (SourceEvns.CLOUD.equals(source.getEnvironment())) {
                if (SourceTypes.ALIYUN.equals(source.getType())) {
                    connectToAliyun(source, tenantId, uuid, operator);
                } else {
                    throw new BusinessException(SourceEvns.CLOUD.name() + "环境不支持的类型：" + source.getType().name());
                }
            } else if (SourceEvns.LOCAL.equals(source.getEnvironment())) {
                if (SourceTypes.MQTT.equals(source.getType())) {
                    connectToMqtt(source, tenantId, uuid, operator);
                } else if (SourceTypes.INFLUXDB.equals(source.getType())) {
                    connectToInfluxDB(source, tenantId, uuid, operator);
                } else {
                    throw new BusinessException(SourceEvns.LOCAL.name() + "环境不支持的类型：" + source.getType().name());
                }
            } else {
                throw new BusinessException("不支持的环境：" + source.getEnvironment().name());
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

    private Source convert(PSource perz) throws BusinessException {
        if (perz == null) {
            return null;
        }

        Source result = new Source();
        BeanUtils.copyProperties(perz, result);
        result.setEnvironment(SourceEvns.valueOf(perz.getEnvironment()));
        result.setType(SourceTypes.valueOf(perz.getType()));
        if (StringUtils.isNotBlank(perz.getParameter())) {
            result.setSourceInfo(convertSourceInfo(result.getType(), perz.getParameter()));
        }

        return result;
    }

    private PSource convert(String tenantId, SourceSaveRq createRq) throws Exception {
        if (createRq == null) {
            return null;
        }

        PSource perz = new PSource();
        perz.setTenantId(tenantId);
        perz.setName(createRq.getName());
        if (createRq.getEnvironment() != null) {
            perz.setEnvironment(createRq.getEnvironment().name());
        }
        if (createRq.getType() != null) {
            perz.setType(createRq.getType().name());
            if (createRq.getSourceInfo() != null) {
                perz.setParameter(convertSourceInfo(createRq.getType(), createRq.getSourceInfo()));
            }
        }

        return perz;
    }


    private void connectToAliyun(Source source, String tenantId, String targetId, String operator) throws Exception {
        if (source.getSourceInfo() == null || source.getSourceInfo().getAliyunSource() == null) {
            throw new BusinessException("参数丢失");
        }
        AliyunSource aliyunSource = source.getSourceInfo().getAliyunSource();
        if (aliyunSource == null) {
            throw new BusinessException("阿里云资源连接参数不能为空");
        }
        if (StringUtils.isBlank(aliyunSource.getAccessKey())) {
            throw new BusinessException("阿里云资源连接参数AccessKey不能为空");
        }
        if (StringUtils.isBlank(aliyunSource.getAccessSecret())) {
            throw new BusinessException("阿里云资源连接参数AccessSecret不能为空");
        }

        try {
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
                changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
                throw new BusinessException("连接失败原因：" + response.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
            throw new BusinessException("连接失败原因：" + e.getMessage(), e);
        }
    }

    private void connectToMqtt(Source source, String tenantId, String targetId, String operator) throws Exception {//// TODO: 2023/2/10
        if (source.getSourceInfo() == null || source.getSourceInfo().getMqttSource() == null) {
            throw new BusinessException("参数丢失");
        }

        try {
            MqttSource mqttSource = source.getSourceInfo().getMqttSource();
            EmqxClient emqxClient = new EmqxClient();
            emqxClient.connect(mqttSource.getHostUrl(), mqttSource.getClientId(), mqttSource.getUsername(), mqttSource.getPassword(), mqttSource.getTimeout(), mqttSource.getKeepalive(), mqttSource.getTopic());
            if (emqxClient.isConnected()) {
                emqxClient.disconnect(null);
                changeConnecting(emqxClient, "emqxClient", tenantId, targetId, operator);
            } else {
                changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
                throw new BusinessException("连接失败！");
            }
        } catch (Exception e) {
            changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
            throw new BusinessException("连接失败原因：" + e.getMessage(), e);
        }
    }


    private void connectToInfluxDB(Source source, String tenantId, String targetId, String operator) throws Exception {
        if (source.getSourceInfo() == null || source.getSourceInfo().getInfluxSource() == null) {
            throw new BusinessException("参数丢失");
        }

        try {
            InfluxSource influxSource = source.getSourceInfo().getInfluxSource();
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
                changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
                throw new BusinessException("连接失败！");
            }
        } catch (Exception e) {
            changeConnectStatus(tenantId, targetId, SourceStatus.CONNECTFAILED, operator);
            throw new BusinessException("连接失败原因：" + e.getMessage(), e);
        }
    }

    private String getConnecting(String tenantId) {
        SourceFilter filter = new SourceFilter();
        filter.setTenantIdEq(tenantId);
        filter.setStatusEq(SourceStatus.CONNECTED);
        filter.setDeletedEq(false);
        List<PSource> list = sourceDao.query(filter);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0).getUuid();
    }

    private void changeConnecting(Object targetObject, String beanName, String tenantId, String id, String operator) throws Exception {
        //查询已连接资源，存在则修改为未连接
        String oldConnectId = getConnecting(tenantId);
        if (StringUtils.isNotBlank(oldConnectId)) {
            disConnect(tenantId, oldConnectId, operator);
        }

        SpringUtil.replaceBean(beanName, targetObject);
        //修改当前资源为已连接
        changeConnectStatus(tenantId, id, SourceStatus.CONNECTED, operator);
    }

    private void changeConnectStatus(String tenantId, String uuid, SourceStatus status, String operator) throws Exception {
        if (status == null) {
            throw new BusinessException("连接状态不能为空");
        }
        PSource perz = sourceDao.get(PSource.class, "uuid", uuid, "tenantId", tenantId, "deleted", false);
        if (perz == null) {
            throw new BusinessException("指定资源信息不存在或已删除");
        }
        if (status.name().equals(perz.getStatus())) {
            return;
        }

        perz.setStatus(status.name());
        perz.onModified(operator);
        sourceDao.save(perz);
    }

    private SourceInfo convertSourceInfo(SourceTypes type, String parameter) {
        SourceInfo info = new SourceInfo();
        if (SourceTypes.ALIYUN.equals(type)) {
            info.setAliyunSource(JsonUtil.jsonToObject(parameter, AliyunSource.class));
        } else if (SourceTypes.MQTT.equals(type)) {
            info.setMqttSource(JsonUtil.jsonToObject(parameter, MqttSource.class));
        } else if (SourceTypes.INFLUXDB.equals(type)) {
            info.setInfluxSource(JsonUtil.jsonToObject(parameter, InfluxSource.class));
        } else {
            //不处理
        }
        return info;
    }

    private String convertSourceInfo(SourceTypes type, SourceInfo info) {
        String parameter = null;
        if (SourceTypes.ALIYUN.equals(type)) {
            parameter = JsonUtil.objectToJson(info.getAliyunSource());
        } else if (SourceTypes.MQTT.equals(type)) {
            parameter = JsonUtil.objectToJson(info.getMqttSource());
        } else if (SourceTypes.INFLUXDB.equals(type)) {
            parameter = JsonUtil.objectToJson(info.getInfluxSource());
        } else {
            //不处理
        }
        return parameter;
    }

    private void checkRelationParam(SourceEvns evn, SourceTypes type, SourceInfo sourceInfo) throws BusinessException {
        if (evn == null) {
            throw new BusinessException("环境不能为空");
        }
        if (type == null) {
            throw new BusinessException("类型不能为空");
        }
        if (sourceInfo == null) {
            throw new BusinessException("连接参数不能为空");
        }

        if (SourceEvns.CLOUD.equals(evn)) {
            if (SourceTypes.ALIYUN.equals(type)) {
                if (sourceInfo.getAliyunSource() == null) {
                    throw new BusinessException("阿里云连接参数不能为空");
                }
                if (StringUtils.isBlank(sourceInfo.getAliyunSource().getAccessKey()) || StringUtils.isBlank(sourceInfo.getAliyunSource().getAccessSecret())) {
                    throw new BusinessException("阿里云连接参数缺失");
                }
            } else {
                throw new BusinessException("资源类型" + type.name() + "与资源环境" + evn.name() + "不匹配");
            }
        } else if (SourceEvns.LOCAL.equals(evn)) {
            if (SourceTypes.MQTT.equals(type)) {
                if (sourceInfo.getMqttSource() == null) {
                    throw new BusinessException("mqtt连接参数不能为空");
                }
                if (StringUtils.isBlank(sourceInfo.getMqttSource().getUsername()) || StringUtils.isBlank(sourceInfo.getMqttSource().getPassword()) || StringUtils.isBlank(sourceInfo.getMqttSource().getHostUrl()) || StringUtils.isBlank(sourceInfo.getMqttSource().getClientId()) || StringUtils.isBlank(sourceInfo.getMqttSource().getTopic())) {
                    throw new BusinessException("mqtt连接参数缺失");
                }
            } else if (SourceTypes.INFLUXDB.equals(type)) {
                if (sourceInfo.getInfluxSource() == null) {
                    throw new BusinessException("influxDB连接参数不能为空");
                }
                if (StringUtils.isBlank(sourceInfo.getInfluxSource().getInfluxDBUrl()) || StringUtils.isBlank(sourceInfo.getInfluxSource().getUserName()) || StringUtils.isBlank(sourceInfo.getInfluxSource().getPassword()) || StringUtils.isBlank(sourceInfo.getInfluxSource().getDatabase())) {
                    throw new BusinessException("influxDB连接参数缺失");
                }
            } else {
                throw new BusinessException("资源类型" + type.name() + "与资源环境" + evn.name() + "不匹配");
            }
        } else {
            throw new BusinessException("不支持的环境：" + evn.name());
        }
    }
}
