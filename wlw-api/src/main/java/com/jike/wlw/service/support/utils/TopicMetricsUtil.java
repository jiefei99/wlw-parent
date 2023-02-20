package com.jike.wlw.service.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.jike.wlw.service.topic.metrics.Metrics;
import com.jike.wlw.service.topic.metrics.TopicMetrics;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicMetricsUtil {

    private static final String HOST = "http://118.31.189.96:18081";
    private static final String PATH = "/api/v4/topic-metrics";
    private static final String METHOD = "GET";
    private static final String APP_KEY = "admin";
    private static final String SECRET_KEY = "public";

    public List<TopicMetrics> getTopicMetrics() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-type", "application/json");
        String auth = getHeader();
        headerMap.put("Authorization", auth);
        Map<String, String> queryMap = new HashMap<>();
        List<TopicMetrics> topicMetrics = new ArrayList<>();
        try {
            HttpResponse response = HttpUtils.doGet(HOST, PATH, METHOD, headerMap, queryMap);
            String resBody = EntityUtils.toString(response.getEntity());
            System.out.println(resBody);
            String data = JSON.parseObject(resBody).getString("data");
            System.out.println(data);
            topicMetrics = JSONArray.parseArray(data, TopicMetrics.class);
            System.out.println();
            int i = 0;
            for (TopicMetrics topicMetric : topicMetrics) {
                String metricsString = JSON.parseArray(data).get(0).toString();
                Metrics metrics = getMetrice(metricsString);
                topicMetric.setMetrics(metrics);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topicMetrics;
    }

    private Metrics getMetrice(String data) {
        Metrics metrics = new Metrics();
        metrics.setMessagesInCount(JSON.parseObject(data).getIntValue("messages.in.count"));
        metrics.setMessagesDroppedRate(JSON.parseObject(data).getIntValue("messages.dropped.rate"));
        metrics.setMessagesOutCount(JSON.parseObject(data).getIntValue("messages.out.count"));
        metrics.setMessagesInRate(JSON.parseObject(data).getIntValue("messages.in.rate"));
        metrics.setMessagesOutRate(JSON.parseObject(data).getIntValue("messages.out.rate"));
        metrics.setMessagesQos0InCount(JSON.parseObject(data).getIntValue("messages.qos0.in.count"));
        metrics.setMessagesQos0OutCount(JSON.parseObject(data).getIntValue("messages.qos0.out.count"));
        metrics.setMessagesQos0InRate(JSON.parseObject(data).getIntValue("messages.qos0.in.rate"));
        metrics.setMessagesQos0OutRate(JSON.parseObject(data).getIntValue("messages.qos0.out.rate"));
        metrics.setMessagesQos1InCount(JSON.parseObject(data).getIntValue("messages.qos1.in.count"));
        metrics.setMessagesQos1OutCount(JSON.parseObject(data).getIntValue("messages.qos1.out.count"));
        metrics.setMessagesQos1InRate(JSON.parseObject(data).getIntValue("messages.qos1.in.rate"));
        metrics.setMessagesQos1OutRate(JSON.parseObject(data).getIntValue("messages.qos1.out.rate"));
        metrics.setMessagesQos2InCount(JSON.parseObject(data).getIntValue("messages.qos2.in.count"));
        metrics.setMessagesQos2OutCount(JSON.parseObject(data).getIntValue("messages.qos2.out.count"));
        metrics.setMessagesQos2InRate(JSON.parseObject(data).getIntValue("messages.qos2.in.rate"));
        metrics.setMessagesQos2OutRate(JSON.parseObject(data).getIntValue("messages.qos2.out.rate"));
        return metrics;
    }

    private String getHeader() {
        String auth = APP_KEY + ":" + SECRET_KEY;
        byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic "+new String(encodeAuth);
        return  authHeader;
    }

    public String login(String name, String password) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username",name);
        userMap.put("password",password);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-type", "application/json");
        StringBuilder token = new StringBuilder();
        token.append("Bearer ");
        try {
            HttpResponse httpResponse = HttpUtils.doPost("http://118.31.189.96:18081", "/api/v4/login","POST",headerMap, new HashMap<>(), JsonUtil.objectToJson(userMap));

//            HttpResponse httpResponse = HttpUtils.doPost("http://localhost:18083", "/api/v5/login",headerMap,new HashMap<>(),userMap);
            System.out.println(httpResponse);
            String resBody = EntityUtils.toString(httpResponse.getEntity());
            token.append(JSON.parseObject(resBody).getString("token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token.toString();
    }
}
