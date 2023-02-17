package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelAttributeFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: PhysicalModelAttributeDao
 * @Author RS
 * @Date: 2023/2/16 17:36
 * @Version 1.0
 */

@Repository
public class PhysicalModelPropertyDao extends BaseDao {
    public List<PPhysicalModelProperty> query(PhysicalModelAttributeFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PPhysicalModelProperty.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PhysicalModelAttributeFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PhysicalModelAttributeFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPhysicalModelProperty.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getModelDeviceId())) {
            q.where("o.modelDeviceId = :modelDeviceId").p("modelDeviceId", filter.getModelDeviceId());
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.id", "desc");
            }
        } else {
            q.orderBy("o.id", "desc");
        }
        return q;
    }

}


