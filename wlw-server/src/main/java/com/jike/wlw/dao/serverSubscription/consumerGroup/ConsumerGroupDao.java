package com.jike.wlw.dao.serverSubscription.consumerGroup;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: ConsumerGroupDao
 * @Author RS
 * @Date: 2023/1/13 15:21
 * @Version 1.0
 */

@Repository
public class ConsumerGroupDao extends BaseDao {

    public List<PConsumerGroup> query(ConsumerGroupFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PConsumerGroup.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(ConsumerGroupFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, ConsumerGroupFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PConsumerGroup.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantId())) {
            q.where("o.tenantId = :tenantId").p("tenantId", filter.getTenantId());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameLike())) {
            q.where("o.name like :nameLike").p("nameLike", "%"+filter.getNameLike()+"%");
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


