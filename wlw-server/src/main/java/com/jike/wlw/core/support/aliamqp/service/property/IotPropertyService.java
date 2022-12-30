package com.jike.wlw.core.support.aliamqp.service.property;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.redis.RedisManager;
import com.jike.wlw.core.support.aliamqp.config.AliIotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("iotPropertyService")
public class IotPropertyService {
    @Resource
    private AliIotProperties aliIotProperties;
    @Autowired
    private RedisManager redisManager;

    private static Map<String, IotPropertyHandle> processMap = new HashMap<>();

    public void process(String messageId, String topic, String content) {
//        String requestId = JSON.parseObject(content).getString("deviceName");
        try {
            //当前暂时不需要（存数据用，先对接）


//            if (redisManager.acquireLock(requestId)) {
//                Map<String, Object> parmas = new HashMap<>();
//                parmas.put("deviceName", requestId);
//
//                IotDevicePropertyData oldIotDevicePropertyData = null;
//                List<IotDevicePropertyData> iotDevicePropertyDatas = MongodbUtils.findByConditionJson(aliIotProperties.getDeviceTemplate(), "device",
//                        JSON.toJSONString(parmas), null, null, IotDevicePropertyData.class, 1, 1);
//                if(ObjectUtil.isNotEmpty(iotDevicePropertyDatas)){
//                     oldIotDevicePropertyData = iotDevicePropertyDatas.get(0);
//                }
//
//                JSONObject contentJson = JSON.parseObject(content);
//                IotDevicePropertyData iotDevicePropertyData = new IotDevicePropertyData();
//                iotDevicePropertyData.setMessageId(messageId);
//                iotDevicePropertyData.setTopic(topic);
//                iotDevicePropertyData.setDeviceName(contentJson.getString("deviceName"));
//                iotDevicePropertyData.setProductKey(contentJson.getString("productKey"));
//                iotDevicePropertyData.setCreateTime(contentJson.getTimestamp("gmtCreate"));
//                iotDevicePropertyData.setItems(contentJson.getJSONObject("items"));
//
//                Map<Object, Object> dataMap = new HashMap<>();
//                dataMap.put("productKey", iotDevicePropertyData.getProductKey());
//                dataMap.put("deviceName", iotDevicePropertyData.getDeviceName());
//                dataMap.put("createTime", iotDevicePropertyData.getCreateTime());
//                dataMap.put("messageId", iotDevicePropertyData.getMessageId());
//                JSONObject items = iotDevicePropertyData.getItems();
//                Set<String> keySet = items.keySet();
//                for (String key : keySet) {
//                    if(ObjectUtil.isNotEmpty(oldIotDevicePropertyData) && ObjectUtil.isNotEmpty(oldIotDevicePropertyData.getItems())){
//                        JSONObject oldKeyData = oldIotDevicePropertyData.getItems().getJSONObject(key);
//                        if(ObjectUtil.isNotEmpty(oldKeyData) && oldKeyData.getTimestamp("time").after(items.getJSONObject(key).getTimestamp("time"))){
//                            continue;
//                        }
//                    }
//
//                    dataMap.put(key, items.get(key));
//                    String handleKey = "heating";
//                    if (key.equals(handleKey)) {
//                        handleTimeLongData(messageId, iotDevicePropertyData, items, handleKey);
//                    }
//                    handleKey = "powerOnSignal";
//                    if (key.equals(handleKey)) {
//                        handleTimeLongData(messageId, iotDevicePropertyData, items, handleKey);
//                    }
//                }

//                MongodbUtils.insert(aliIotProperties.getDeviceTemplate(), dataMap, "deviceLog");
//                Map<String, Object> matchMap = new HashMap<>();
//                matchMap.put("deviceName", iotDevicePropertyData.getDeviceName());
//                MongodbUtils.upsertByFind(aliIotProperties.getDeviceTemplate(), "device", matchMap, dataMap);


//                if (processMap.containsKey(iotDevicePropertyData.getProductKey())) {
//                    IIotPropertyHandle handler = processMap.get(iotDevicePropertyData.getProductKey());
//                    Object data = handler.process(iotDevicePropertyData);
//                }
//            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
//        finally {
//            redisManager.releaseLock(requestId);
//        }
    }


    public static void registHandle(String productKey, IotPropertyHandle iotPropertyHandle) {
        processMap.put(productKey, iotPropertyHandle);
    }
}
