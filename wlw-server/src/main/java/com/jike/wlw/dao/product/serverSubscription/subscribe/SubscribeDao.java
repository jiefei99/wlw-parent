package com.jike.wlw.dao.product.serverSubscription.subscribe;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.product.serverSubscription.subscribe.SubscribeFilter;
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
        return q.list(jdbcTemplate, PSubscribe.class, filter.getCurrentPage(), filter.getPageSize());
    }

    public long getCount(SubscribeFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
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
        if (!StringUtil.isNullOrBlank(filter.getIotInstanceId())) {
            q.where("o.productKey = :productKey").p("productKey", filter.getProductKey());
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


