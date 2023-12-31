package com.jike.wlw.dao.physicalmodel.function;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.function.FunctionFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class FunctionDao extends BaseDao {

    public List<PFunction> query(FunctionFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PFunction.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(FunctionFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, FunctionFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PFunction.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.id = :id").p("id", filter.getIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        if (filter.getTypeEq() != null) {
            q.where("o.type = :typeEq").p("typeEq", filter.getTypeEq().name());
        }
        if (filter.getAccessModeEq() != null) {
            q.where("o.accessMode = :accessModeEq").p("accessModeEq", filter.getAccessModeEq().name());
        }
        if (filter.getRequiredEq() != null) {
            q.where("o.required = :requiredEq").p("requiredEq", filter.getRequiredEq());
        }
        //in查询
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.id in  (:ids)").p("ids", filter.getIdIn());
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
