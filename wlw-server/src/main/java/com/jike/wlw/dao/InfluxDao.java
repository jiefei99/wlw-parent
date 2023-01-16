package com.jike.wlw.dao;

import com.jike.wlw.util.SpringUtil;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class InfluxDao {

    //数据保存
    public void saveMessage(String measurement, Map<String, Object> fields, Map<String, String> tags) {
        Point.Builder builder = Point.measurement(measurement);
        builder.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .fields(fields).tag(tags);
        SpringUtil.getBean(InfluxDB.class).write(builder.build());
    }

    //数据读取
    public QueryResult getMessage(String measurement, String command) {
        Query query = new Query(command, measurement);
        QueryResult queryResult = SpringUtil.getBean(InfluxDB.class).query(query);
        return queryResult;
    }

    /**
     * 查询，返回Map集合
     *
     * @param query 完整的查询语句
     * @return
     */

    public List<Map<String, Object>> fetchRecords(String measurement, String query) {
        List<Map<String, Object>> results = new ArrayList<>();

        QueryResult queryResult = SpringUtil.getBean(InfluxDB.class).query(new Query(query, measurement));

        queryResult.getResults().forEach(result -> {
            result.getSeries().forEach(serial -> {
                List<String> columns = serial.getColumns();
                int fieldSize = columns.size();
                serial.getValues().forEach(value -> {
                    Map<String, Object> obj = new HashMap<>();
                    for (int i = 0; i < fieldSize; i++) {
                        obj.put(columns.get(i), value.get(i));
                    }
                    results.add(obj);
                });
            });
        });

        return results;
    }

    /**
     * 查询，返回对象的list集合
     *
     * @param query
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> fetchResults(String measurement, String query, Class<?> clasz) {
        List results = new ArrayList<>();
        QueryResult queryResult = SpringUtil.getBean(InfluxDB.class).query(new Query(query, measurement));

        queryResult.getResults().forEach(result -> {
            result.getSeries().forEach(serial -> {
                List<String> columns = serial.getColumns();
                int fieldSize = columns.size();
                serial.getValues().forEach(value -> {
                    Object obj = null;
                    try {
                        obj = clasz.newInstance();
                        for (int i = 0; i < fieldSize; i++) {
                            String fieldName = columns.get(i);
                            Field field = clasz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Class<?> type = field.getType();
                            if (type == float.class) {
                                field.set(obj, Float.valueOf(value.get(i).toString()));
                            } else {
                                field.set(obj, value.get(i));
                            }
                        }
                    } catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    results.add(obj);
                });
            });
        });

        return results;
    }

}