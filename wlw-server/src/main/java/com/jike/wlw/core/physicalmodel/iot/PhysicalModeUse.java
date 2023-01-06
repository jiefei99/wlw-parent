/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年01月05日 11:26 - ASUS - 创建。
 */
package com.jike.wlw.core.physicalmodel.iot;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author rs
 * @since 1.0
 */

@Slf4j
@Service
public class PhysicalModeUse {
    @Autowired
    private Environment env;

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config();
        // 您的 AccessKey ID
        config.setAccessKeyId(accessKeyId);
        // 您的 AccessKey Secret
        config.setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

    public SetDevicePropertyResponse setDeviceProperty(PhysicalModelUseRq model) throws Exception {
        if (model.getItems()==null){
            throw new IllegalAccessException("物模型属性信息不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        SetDevicePropertyRequest request=new SetDevicePropertyRequest();
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        //重要 IotId作为设备唯一标识符
        request.setIotId(model.getIotId());
        //若有ID值，必须传入该ID值，否则调用会失败。
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        //设置的属性信息
        request.setItems(new JSONObject(model.getItems()).toString());
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SetDevicePropertyResponse response = client.setDevicePropertyWithOptions(request, runtime);
            System.out.println("设置属性值返回结果：" + response);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalAccessException(e.getMessage());
        }
    }

    public SetDevicesPropertyResponse SetDevicesProperty(PhysicalModelUseRq model) throws Exception {
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("设备所属的产品productKey不能为空");
        }
        if (CollectionUtils.isEmpty(model.getDeviceNameList())){
            throw new IllegalAccessException("设置属性值的设备名称不能为空");
        }
        if (model.getItems()==null){
            throw new IllegalAccessException("物模型属性信息不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        SetDevicesPropertyRequest request=new SetDevicesPropertyRequest();
        request.setDeviceName(model.getDeviceNameList());
        request.setProductKey(model.getProductKey());
        request.setIotInstanceId(model.getIotInstanceId());
        //设置的属性信息
        request.setItems(new JSONObject(model.getItems()).toString());
        SetDevicesPropertyResponse response = client.setDevicesProperty(request);
        System.out.println("设置多个属性值返回结果：" + response);
        return response;
    }

    public InvokeThingServiceResponse invokeThingService(PhysicalModelUseRq model) throws Exception {
        if (StringUtils.isBlank(model.getIdentifier())){
            throw new IllegalAccessException("物模型服务的标识符不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        InvokeThingServiceRequest request=new InvokeThingServiceRequest();
        request.setIotId(model.getIotId());
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        //为空传递{}
        if (model.getArgs()!=null){
            request.setArgs(new JSONObject(model.getArgs()).toString());
        }else {
            request.setArgs("{}");
        }
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        request.setIdentifier(model.getIdentifier());
        InvokeThingServiceResponse response = client.invokeThingService(request);
        return response;
    }

    public InvokeThingsServiceResponse invokeThingsService(PhysicalModelUseRq model) throws Exception {
        if (CollectionUtils.isEmpty(model.getDeviceNameList())){
            throw new IllegalAccessException("物模型调用服务的设备的名称列表不能为空");
        }
        if (StringUtils.isBlank(model.getIdentifier())){
            throw new IllegalAccessException("物模型服务的标识符不能为空");
        }
        if (StringUtils.isBlank(model.getProductKey())){
            throw new IllegalAccessException("设备所属的产品productKey不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        InvokeThingsServiceRequest request=new InvokeThingsServiceRequest();
        //为空传递{}
        if (model.getArgs()!=null){
            request.setArgs(new JSONObject(model.getArgs()).toString());
        }else {
            request.setArgs("{}");
        }
        request.setDeviceName(model.getDeviceNameList());
        request.setProductKey(model.getProductKey());
        //若有ID值，必须传入该ID
        request.setIotInstanceId(model.getIotInstanceId());
        request.setIdentifier(model.getIdentifier());
        InvokeThingsServiceResponse response = client.invokeThingsService(request);
        return response;
    }

    public QueryDevicePropertyDataResponse queryDevicePropertyData(PhysicalModelUseRq model) throws Exception {
        if (StringUtils.isBlank(model.getEndTime())||StringUtils.isBlank(model.getStartTime())){
            throw new IllegalAccessException("物模型属性记录开始、结束时间不能为空");
        }
        if (StringUtils.isBlank(model.getIdentifier())){
            throw new IllegalAccessException("物模型服务的标识符不能为空");
        }
        if (StringUtils.isBlank(model.getPageSize())||(Integer.valueOf(model.getPageSize())>50||Integer.valueOf(model.getPageSize())<0)){
            throw new IllegalAccessException("物模型返回结果中每页显示的记录数不能为空且记录数不能大于50");
        }
        if (model.getAsc()!=1||model.getAsc()!=2){
            throw new IllegalAccessException("物模型排序的方式必须为正序或者倒序");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryDevicePropertyDataRequest request=new QueryDevicePropertyDataRequest();
        request.setIotId(model.getIotId());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        request.setIdentifier(model.getIdentifier());
        request.setAsc(model.getAsc());
        request.setEndTime(Long.valueOf(model.getEndTime()));
        request.setPageSize(Integer.valueOf(model.getPageSize()));
        request.setStartTime(Long.valueOf(model.getStartTime()));
        QueryDevicePropertyDataResponse response = client.queryDevicePropertyData(request);
        return response;
    }

    public QueryDevicePropertiesDataResponse queryDevicePropertiesData(PhysicalModelUseRq model) throws Exception {
        if (model.getAsc()!=1||model.getAsc()!=2){
            throw new IllegalAccessException("物模型排序的方式必须为正序或者倒序");
        }
        if (StringUtils.isBlank(model.getPageSize())||(Integer.valueOf(model.getPageSize())>50||Integer.valueOf(model.getPageSize())<0)){
            throw new IllegalAccessException("物模型返回结果中每页显示的记录数不能为空且记录数不能大于50");
        }
        if (StringUtils.isBlank(model.getEndTime())||StringUtils.isBlank(model.getStartTime())){
            throw new IllegalAccessException("物模型属性记录开始、结束时间不能为空");
        }
        if (CollectionUtils.isEmpty(model.getIdentifierList())){
            throw new IllegalAccessException("属性的标识符列表不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryDevicePropertiesDataRequest request=new QueryDevicePropertiesDataRequest();
        request.setIotId(model.getIotId());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        request.setIdentifier(model.getIdentifierList());
        request.setAsc(model.getAsc());
        request.setEndTime(Long.valueOf(model.getEndTime()));
        request.setPageSize(Integer.valueOf(model.getPageSize()));
        request.setStartTime(Long.valueOf(model.getStartTime()));
        QueryDevicePropertiesDataResponse queryDevicePropertiesDataResponse = client.queryDevicePropertiesData(request);
        return queryDevicePropertiesDataResponse;
    }

    public QueryDeviceEventDataResponse queryDeviceEventData(PhysicalModelUseRq model) throws Exception{
        if (StringUtils.isBlank(model.getEndTime())||StringUtils.isBlank(model.getStartTime())){
            throw new IllegalAccessException("物模型属性记录开始、结束时间不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryDeviceEventDataRequest request=new QueryDeviceEventDataRequest();
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        request.setAsc(model.getAsc());
        request.setIotId(model.getIotId());
        request.setIdentifier(model.getIdentifier());
        request.setEndTime(Long.valueOf(model.getEndTime()));
        request.setEventType(model.getEventType());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        request.setStartTime(Long.valueOf(model.getStartTime()));
        request.setPageSize(Integer.valueOf(model.getPageSize()));
        QueryDeviceEventDataResponse response = client.queryDeviceEventData(request);
        return response;
    }

    //QueryDeviceServiceData
    public QueryDeviceServiceDataResponse queryDeviceServiceData(PhysicalModelUseRq model) throws Exception {
        if (StringUtils.isBlank(model.getEndTime())||StringUtils.isBlank(model.getStartTime())){
            throw new IllegalAccessException("物模型属性记录开始、结束时间不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryDeviceServiceDataRequest request=new QueryDeviceServiceDataRequest();
        request.setAsc(model.getAsc());
        request.setIotId(model.getIotId());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        request.setIdentifier(model.getIdentifier());
        request.setEndTime(Long.valueOf(model.getEndTime()));
        request.setPageSize(Integer.valueOf(model.getPageSize()));
        request.setStartTime(Long.valueOf(model.getStartTime()));
        QueryDeviceServiceDataResponse response = client.queryDeviceServiceData(request);
        return  response;
    }

    //SetDeviceDesiredProperty
    public SetDeviceDesiredPropertyResponse setDeviceDesiredProperty(PhysicalModelUseRq model) throws Exception{
        if (model.getItems()==null){
            throw new IllegalAccessException("物模型设置的期望属性值不能为空");
        }
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        SetDeviceDesiredPropertyRequest request=new SetDeviceDesiredPropertyRequest();
        request.setIotId(model.getIotId());
        request.setItems(new JSONObject(model.getItems()).toString());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        request.setVersions(new JSONObject(model.getVersions()).toString());
        SetDeviceDesiredPropertyResponse response = client.setDeviceDesiredProperty(request);
        return response;

    }

    //QueryDeviceDesiredProperty
    public QueryDeviceDesiredPropertyResponse queryDeviceDesiredProperty(PhysicalModelUseRq model) throws Exception{
        Client client = createClient(env.getProperty("ali.iot.accessKey"), env.getProperty("ali.iot.accessSecret"));
        QueryDeviceDesiredPropertyRequest request =new QueryDeviceDesiredPropertyRequest();
        request.setIotId(model.getIotId());
        //若有ID值，必须传入该ID
        if (StringUtils.isNotBlank(model.getIotInstanceId())){
            request.setIotInstanceId(model.getIotInstanceId());
        }
        request.setIdentifier(model.getIdentifierList());
        request.setFunctionBlockId(model.getFunctionBlockId());
        if (StringUtils.isNotBlank(model.getDeviceName())&&StringUtils.isNotBlank(model.getProductKey())){
            //如果传入该参数，需同时传入ProductKey。
            request.setDeviceName(model.getDeviceName());
            //如果传入该参数，需同时传入DeviceName。
            request.setProductKey(model.getProductKey());
        }
        QueryDeviceDesiredPropertyResponse response = client.queryDeviceDesiredProperty(request);
        return response;
    }
}
