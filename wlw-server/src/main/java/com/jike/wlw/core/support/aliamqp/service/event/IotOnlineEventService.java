package com.jike.wlw.core.support.aliamqp.service.event;

import cn.hutool.core.util.IdUtil;
import com.jike.wlw.core.support.aliamqp.config.AliIotProperties;
import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceOnlineData;
import com.jike.wlw.core.support.aliamqp.model.property.IotDeviceFieldTimeLongData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class IotOnlineEventService {
     private static List<IotOnlineEventHandle> processList = new ArrayList<>();
     @Resource
     private AliIotProperties aliIotProperties;
     public void process(IotDeviceOnlineData data){

          String msgId = IdUtil.simpleUUID();
          Map<Object, Object> dataMap = new HashMap<>();
          dataMap.put("productKey",data.getProductKey());
          dataMap.put("deviceName",data.getDeviceName());
          dataMap.put("createTime",data.getTime());
          dataMap.put("messageId", msgId);

          Map<Object, Object> valueMap = new HashMap<>();
          valueMap.put("time",data.getTime());
          valueMap.put("value",data.getStatus());
		  dataMap.put("onlineStatus",valueMap);

          IotDeviceFieldTimeLongData newIotDeviceFieldTimeLongData = new IotDeviceFieldTimeLongData();
          newIotDeviceFieldTimeLongData.setBeginDeviceTime(data.getTime());
          newIotDeviceFieldTimeLongData.setDeviceName(data.getDeviceName());
          newIotDeviceFieldTimeLongData.setBeginMessageId(msgId);
          newIotDeviceFieldTimeLongData.setKey("onlineStatus");
          newIotDeviceFieldTimeLongData.setProductKey(data.getProductKey());
          newIotDeviceFieldTimeLongData.setBeingValue(data.getStatus());
          newIotDeviceFieldTimeLongData.setCreateTime(new Date());


          //获取历史数据
//          Map<String, Object> parmas = new HashMap<>();
//          parmas.put("deviceName",newIotDeviceFieldTimeLongData.getDeviceName());
//          parmas.put("key",newIotDeviceFieldTimeLongData.getKey());
//
//          Map<String, Object> sortParams = new HashMap<>();
//          sortParams.put("beginDeviceTime",-1);
//
//          List<IotDeviceFieldTimeLongData> oldIotDeviceFieldTimeLongDatas = MongodbUtils.findByConditionJson(aliIotProperties.getDeviceTemplate(),  "deviceTimeLongLog",
//                  JSON.toJSONString(parmas) , null,JSON.toJSONString(sortParams),IotDeviceFieldTimeLongData.class, 1, 1);
//
//          if(ObjectUtil.isNotEmpty(oldIotDeviceFieldTimeLongDatas)){
//               IotDeviceFieldTimeLongData    oldIotDeviceFieldTimeLongData = oldIotDeviceFieldTimeLongDatas.get(0);
//               oldIotDeviceFieldTimeLongData.setEndDeviceTime(data.getTime());
//               oldIotDeviceFieldTimeLongData.setEndMessageId(msgId);
//               oldIotDeviceFieldTimeLongData.setEndValue(data.getStatus());
//               long betweenMs = DateUtil.betweenMs(oldIotDeviceFieldTimeLongData.getBeginDeviceTime(),oldIotDeviceFieldTimeLongData.getEndDeviceTime() );
//               oldIotDeviceFieldTimeLongData.setBetweenTime(betweenMs);
//               Map<String, Object> updateParams = new HashMap<>();
//               updateParams.put("beginMessageId",oldIotDeviceFieldTimeLongData.getBeginMessageId());
//               updateParams.put("key",newIotDeviceFieldTimeLongData.getKey());
//               MongodbUtils.upsertByFind(aliIotProperties.getDeviceTemplate(), "deviceTimeLongLog", updateParams, oldIotDeviceFieldTimeLongData);
//
//          }
//
//          MongodbUtils.insert(aliIotProperties.getDeviceTemplate(),newIotDeviceFieldTimeLongData, "deviceTimeLongLog");
//          //插入点位历史日志
//          MongodbUtils.insert(aliIotProperties.getDeviceTemplate(),dataMap,"deviceLog");
//          //插入点位时长表
//          Map<String, Object> matchMap = new HashMap<>();
//          matchMap.put("deviceName",newIotDeviceFieldTimeLongData.getDeviceName());
//          MongodbUtils.upsertByFind(aliIotProperties.getDeviceTemplate(), "device", matchMap, dataMap);



     };

     public static void  registHandle(IotOnlineEventHandle iotOnlineEventHandle){
          processList.add(iotOnlineEventHandle);
     }


}
