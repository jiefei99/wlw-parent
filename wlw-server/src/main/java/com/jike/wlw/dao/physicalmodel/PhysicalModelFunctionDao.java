package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelFunctionFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title: PhysicalModelServiceDao
 * @Author RS
 * @Date: 2023/2/16 17:37
 * @Version 1.0
 */

@Repository
public class PhysicalModelFunctionDao extends BaseDao {
    public List<PPhysicalModelFunction> query(PhysicalModelFunctionFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PPhysicalModelFunction.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PhysicalModelFunctionFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PhysicalModelFunctionFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPhysicalModelFunction.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getModelDeviceId())) {
            q.where("o.modelDeviceId = :modelDeviceId").p("modelDeviceId", filter.getModelDeviceId());
        }
        if (filter.getType()!=null) {
            q.where("o.type = :type").p("type", filter.getType());
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


