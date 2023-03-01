package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelDataStandardFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @title: PhysicalModelDataTypeStandardDao
 * @Author RS
 * @Date: 2023/2/16 17:40
 * @Version 1.0
 */

@Repository
public class PhysicalModelDataStandardDao extends BaseDao {
    public List<PPhysicalModelDataStandard> query(PhysicalModelDataStandardFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PPhysicalModelDataStandard.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PhysicalModelDataStandardFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PhysicalModelDataStandardFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPhysicalModelDataStandard.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getParentIdEq())) {
            q.where("o.parentId = :parentId").p("parentId", filter.getParentIdEq());
        }
        if (!CollectionUtils.isEmpty(filter.getParentIdIn())) {
            q.where("o.parentId in (:parentIdIn)").p("parentIdIn", filter.getParentIdIn());
        }
        q.where("o.isDeleted = 0");
        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.uuid", "desc");
            }
        } else {
            q.orderBy("o.uuid", "desc");
        }
        return q;
    }
}


