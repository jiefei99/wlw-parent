package com.jike.wlw.dao.serverSubscription.subscribe;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeFilter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: SubscribeDao
 * @Author RS
 * @Date: 2023/1/16 14:16
 * @Version 1.0
 */

@Repository
public class SubscribeDao extends BaseDao {

    public List<PSubscribe> query(SubscribeFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PSubscribe.class);
    }

    public long getCount(SubscribeFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    public List<String> queryProductKeys() {
        String sql="SELECT DISTINCT productKey FROM "+PSubscribe.TABLE_NAME;
        return jdbcTemplate.queryForList(sql,String.class);
    }

    public void removeSubscribe(String tenantId, String type, String productKey) {
        String sql = "delete from " + PSubscribe.TABLE_NAME + " where type = '" + type + "' and productKey = '" + productKey + "' and tenantId= '" + tenantId + "'";
        jdbcTemplate.batchUpdate(sql);
    }

    public void removeSubscribeByGroupId(String tenantId, String groupId, String productKey) {
        String sql = "delete from " + PSubscribe.TABLE_NAME + " where consumerGroupId = '" + groupId + "' and productKey = '" + productKey + "' and tenantId= '" + tenantId + "'";
        jdbcTemplate.batchUpdate(sql);
    }

    private JdbcEntityQuery getQuery(String name, String select, SubscribeFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PSubscribe.TABLE_NAME, "o");
        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantId())) {
            q.where("o.tenantId = :tenantId").p("tenantId", filter.getTenantId());
        }
        if (!StringUtil.isNullOrBlank(filter.getType())) {
            q.where("o.type = :type").p("type", filter.getType());
        }
        if (!StringUtil.isNullOrBlank(filter.getProductKey())) {
            q.where("o.productKey = :productKey").p("productKey", filter.getProductKey());
        }
        if (!StringUtil.isNullOrBlank(filter.getGroupIdEq())) {
            q.where("o.consumerGroupId = :consumerGroupId").p("consumerGroupId", filter.getGroupIdEq());
        }
        q.where("o.isDeleted = 0");

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.created", "desc");
            }
        } else {
            q.orderBy("o.created", "desc");
        }
        return q;
    }

}


