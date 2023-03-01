package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelFilter;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModuleFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhysicalModelModuleDao extends BaseDao {

    public List<PPhysicalModelModule> query(PhysicalModelModuleFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PPhysicalModelModule.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PhysicalModelModuleFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PhysicalModelModuleFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPhysicalModelModule.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantId())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantId());
        }
        if (!StringUtil.isNullOrBlank(filter.getProductKey())) {
            q.where("o.productKey = :productKey").p("productKey", filter.getProductKey());
        }
        if (!StringUtil.isNullOrBlank(filter.getIdentifierEq())) {
            q.where("o.identifier = :identifierEq").p("identifierEq", filter.getIdentifierEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
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
