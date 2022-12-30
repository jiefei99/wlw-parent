package com.jike.wlw.core.support.aliamqp.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.geeker123.rumba.commons.util.AppCtxUtil;
import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceEventData;
import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceEventHeadData;
import com.jike.wlw.core.support.aliamqp.model.event.IotDeviceOnlineData;
import com.jike.wlw.core.support.aliamqp.service.event.IotEventHandle;
import com.jike.wlw.core.support.aliamqp.service.event.IotOnlineEventService;
import com.jike.wlw.core.support.aliamqp.service.property.IotPropertyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class AliIotConfig {
    @Resource
    private AliIotProperties aliIotProperties;
    private static Map<String, IotEventHandle> iotEventHandleMap = new HashMap<>();

    public static void registHandle(String eventType, IotEventHandle iotEventHandle) {
        iotEventHandleMap.put(eventType, iotEventHandle);
    }

    //业务处理异步线程池，线程池参数可以根据您的业务特点调整，或者您也可以用其他异步方式处理接收到的消息。
    private final static ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(50000));

    @Bean("iotClient")
    public DefaultAcsClient connect() throws Exception {
        log.info("启动阿里云物联网平台对接........");
        String regionId = aliIotProperties.getRegionId();

        String accessKey = aliIotProperties.getAccessKey();// "LTAIZOpGhq6KtGqU";
        String accessSecret = aliIotProperties.getAccessSecret();//"xi2neJPmjJqDOmtjzTL9pBq8yLXogZ";
        String consumerGroupId = aliIotProperties.getConsumerGroupId();//"jmz9pAJ1HzawVqA2N3r9000100";  //去服务端订阅页面的消费组列表查
        long timeStamp = System.currentTimeMillis();
        //签名方法：支持hmacmd5，hmacsha1和hmacsha256
        String signMethod = "hmacsha1";
        //控制台服务端订阅中消费组状态页客户端ID一栏将显示clientId参数。
        //建议使用机器UUID、MAC地址、IP等唯一标识等作为clientId。便于您区分识别不同的客户端。
        String clientId = aliIotProperties.getClientId();//"1730751346747413";;  //随意取
        if (clientId == null || clientId.trim().equals("")) {
            clientId = UUID.randomUUID().toString();
        }
        String iotInstanceId = aliIotProperties.getInstanceId();
        //UserName组装方法，请参见上一篇文档：AMQP客户端接入说明。
        String userName = clientId + "|authMode=aksign"
                + ",signMethod=" + signMethod
                + ",timestamp=" + timeStamp
                + ",authId=" + accessKey
                + ",iotInstanceId=" + iotInstanceId
                + ",consumerGroupId=" + consumerGroupId
                + "|";
        //password组装方法，请参见上一篇文档：AMQP客户端接入说明。
        String signContent = "authId=" + accessKey + "&timestamp=" + timeStamp;
        try {
            String password = doSign(signContent, accessSecret, signMethod);
            //按照qpid-jms的规范，组装连接URL。
            //麻烦看看这里的注释，用户id点击右上角的头像，进入个人信息页面查询，你的地区id(我的是zn-shanghai)
            String productKeys = aliIotProperties.getProductKeys();//"failover:(amqps://1730751346747413.iot-amqp.cn-shanghai.aliyuncs.com:5671?amqp.idleTimeout=80000)?failover.reconnectDelay=30";
            String[] productKeyArray = productKeys.split(",");
            for (String productKey : productKeyArray) {
                Hashtable<String, String> hashtable = new Hashtable<String, String>();
                //  1539609803551574   uid
                //  261502003422096336
                String connectionUrl = "failover:(amqps://" + "1539609803551574" + ".iot-amqp.cn-shanghai.aliyuncs.com:5671?amqp.idleTimeout=80000)?failover.reconnectDelay=30";
                hashtable.put("connectionfactory.SBCF", connectionUrl);
                hashtable.put("queue.QUEUE", "default");
                hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
                Context context = new InitialContext(hashtable);
                ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
                Destination queue = (Destination) context.lookup("QUEUE");
                // 创建连接
                Connection connection = cf.createConnection(userName, password);
                ((JmsConnection) connection).addConnectionListener(myJmsConnectionListener);
                // 创建绘画
                // Session.CLIENT_ACKNOWLEDGE: 收到消息后，需要手动调用message.acknowledge()
                // Session.AUTO_ACKNOWLEDGE: SDK自动ACK（推荐）
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                connection.start();  //TODO
                // 创建Receiver连接
                MessageConsumer consumer = session.createConsumer(queue);
                consumer.setMessageListener(messageListener);
            }

        } catch (Exception e) {
            log.error("阿里云物联网平台对接失败........");
            throw new Exception(e);
        }


        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
            DefaultProfile.addEndpoint(regionId, regionId, "Iot", "iot." + regionId + ".aliyuncs.com");
            DefaultAcsClient client = new DefaultAcsClient(profile);
            log.info("阿里云物联网平台对接成功........");
            return client;
        } catch (Exception e) {
            log.error("阿里云物联网平台对接失败........");
            throw new Exception(e);
        }
    }

    /**
     * 计算签名，password组装方法，请参见AMQP客户端接入说明文档。
     */
    private static String doSign(String toSignString, String secret, String signMethod) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), signMethod);
        Mac mac = Mac.getInstance(signMethod);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(toSignString.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }

    private JmsConnectionListener myJmsConnectionListener = new JmsConnectionListener() {

        @Override
        public void onConnectionEstablished(URI uri) {
            log.info("连接成功" + uri);
        }

        @Override
        public void onConnectionFailure(Throwable throwable) {
            log.error("连接失败" + throwable);

        }

        @Override
        public void onConnectionInterrupted(URI uri) {

        }

        @Override
        public void onConnectionRestored(URI uri) {

        }

        @Override
        public void onInboundMessage(JmsInboundMessageDispatch jmsInboundMessageDispatch) {

        }

        @Override
        public void onSessionClosed(Session session, Throwable throwable) {

        }

        @Override
        public void onConsumerClosed(MessageConsumer messageConsumer, Throwable throwable) {

        }

        @Override
        public void onProducerClosed(MessageProducer messageProducer, Throwable throwable) {

        }
    };

    private static MessageListener messageListener = new MessageListener() {
        @Override
        public void onMessage(final Message message) {

            executorService.submit(() -> processMessage(message));

//            TaskExecutor taskExecutor = AppCtxUtil.getBean("wlw.extractExecutor");
//            taskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        processMessage(message);
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
    };

    private static void processMessage(Message message) {
        try {
            byte[] body = message.getBody(byte[].class);
            String content = new String(body);
            String topic = message.getStringProperty("topic");
            String messageId = message.getStringProperty("messageId");

            if (topic.contains("/as/mqtt/status")) {
                IotOnlineEventService iotOnlineEventService = AppCtxUtil.getBean(IotOnlineEventService.class);
                IotDeviceOnlineData csDeviceOnlineVo = JSON.parseObject(content, IotDeviceOnlineData.class);
                iotOnlineEventService.process(csDeviceOnlineVo);
                return;
            }
            if (topic.contains("thing/event/property/post")) {
                IotPropertyService iotPropertyService = AppCtxUtil.getBean(IotPropertyService.class);
                iotPropertyService.process(messageId, topic, content);
                return;
            }

            JSONObject json = JSONObject.parseObject(content);
            JSONObject items = JSONObject.parseObject(json.getString("items"));
            if (ObjectUtil.isNull(items)) {
                return;
            }

            Set<String> keySet = items.keySet();
            for (String eventType : keySet) {
                JSONObject eventJson = items.getJSONObject(eventType);
                IotDeviceEventHeadData eventHeadData = new IotDeviceEventHeadData();
                eventHeadData.setDeviceName(json.getString("deviceName"));
                eventHeadData.setDeviceType(json.getString("deviceType"));
                eventHeadData.setEventType(eventType);
                eventHeadData.setIotId(json.getString("iotId"));
                eventHeadData.setMessageId(messageId);
                eventHeadData.setTopic(topic);
                eventHeadData.setRequestId(json.getString("requestId"));
                IotEventHandle iotEventHandle = iotEventHandleMap.get(eventType);
                IotDeviceEventData eventData = new IotDeviceEventData();
                eventData.setHeadData(eventHeadData);
                eventData.setEventData(eventJson.getJSONObject("value"));
                iotEventHandle.process(eventType, eventData);
                break;
            }
        } catch (Exception e) {
            log.error("processMessage occurs error ", e);
        }
    }

}
