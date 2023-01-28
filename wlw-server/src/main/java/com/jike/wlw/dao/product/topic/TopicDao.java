package com.jike.wlw.dao.product.topic;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.product.topic.TopicFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: TopicDao
 * @Author RS
 * @Date: 2023/1/16 17:48
 * @Version 1.0
 */
@Repository
public class TopicDao extends BaseDao {
    public List<PTopic> get(TopicFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PTopic.class);
    }

    public long getCount(TopicFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, TopicFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PTopic.TABLE_NAME, "o");
        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantId())) {
            q.where("o.tenantId = :tenantId").p("tenantId", filter.getTenantId());
        }
        if (!StringUtil.isNullOrBlank(filter.getProductKey())) {
            q.where("o.productKey = :productKey").p("productKey", filter.getProductKey());
        }
        if (!StringUtil.isNullOrBlank(filter.getId())) {
            q.where("o.id = :id").p("id", filter.getId());
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


