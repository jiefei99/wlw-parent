package com.jike.wlw.service.support.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.jike.wlw.service.topic.metrics.Metrics;
import com.jike.wlw.service.topic.metrics.TopicMetrics;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicMetricsUtil {

    private static final String HOST = "http://192.168.1.17:18083";
    private static final String PATH = "/api/v5/mqtt/topic_metrics";
    private static final String METHOD = "GET";

    public List<TopicMetrics> getTopicMetrics() {
        Map<String, String> headerMap = new HashMap<>();
        String token = login("admin", "11111111.");
        headerMap.put("Content-type", "application/json");
        headerMap.put("Authorization", token);

        List<TopicMetrics> topicMetricsList = new ArrayList<>();
        Map<String, String> queryMap = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doGet(HOST, PATH, METHOD, headerMap, queryMap);
            String resBody = EntityUtils.toString(response.getEntity());
            topicMetricsList = JSONArray.parseArray(resBody, TopicMetrics.class);
            JSONArray objects = JSON.parseArray(resBody);
            int i = 0;
            for (TopicMetrics topicMetric : topicMetricsList) {
                String metricsString = JSON.parseObject(objects.get(i).toString()).getString("metrics");
                Metrics metrics = getMetrice(metricsString);
                topicMetric.setMetrics(metrics);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topicMetricsList;
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

    public String login(String name, String password) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username",name);
        userMap.put("password",password);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-type", "application/json");
        StringBuilder token = new StringBuilder();
        token.append("Bearer ");
        try {
            HttpResponse httpResponse = HttpUtils.doPost("http://192.168.1.17:18083", "/api/v5/login","POST",headerMap, new HashMap<>(), JsonUtil.objectToJson(userMap));

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
