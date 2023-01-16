package com.jike.wlw.dao.source;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.source.SourceFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class SourceDao extends BaseDao {

    public List<PSource> query(SourceFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PSource.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(SourceFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, SourceFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PSource.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getEnvironmentEq())) {
            q.where("o.environment = :environmentEq").p("environmentEq", filter.getEnvironmentEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getTypeEq())) {
            q.where("o.type = :typeEq").p("typeEq", filter.getTypeEq());
        }
        if (filter.getDeletedEq() != null) {
            q.where("o.deleted = :deletedEq").p("deletedEq", filter.getDeletedEq());
        }
        if (filter.getConnectedEq() != null) {
            q.where("o.connected = :connectedEq").p("connectedEq", filter.getConnectedEq());
        }
        //in查询
        if (!CollectionUtils.isEmpty(filter.getNameIn())) {
            q.where("o.name in  (:nameIn)").p("nameIn", filter.getNameIn());
        }
        if (!CollectionUtils.isEmpty(filter.getEnvironmentIn())) {
            q.where("o.id in  (:environmentIn)").p("environmentIn", filter.getEnvironmentIn());
        }
        if (!CollectionUtils.isEmpty(filter.getTypeIn())) {
            q.where("o.id in  (:typeIn)").p("typeIn", filter.getTypeIn());
        }

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
