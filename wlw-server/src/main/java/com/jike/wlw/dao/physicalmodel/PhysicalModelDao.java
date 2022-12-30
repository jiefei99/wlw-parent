package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.physicalmodel.PhysicalModelFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class PhysicalModelDao extends BaseDao {

    public List<PPhysicalModel> query(PhysicalModelFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PPhysicalModel.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PhysicalModelFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PhysicalModelFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPhysicalModel.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.id = :id").p("id", filter.getIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
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
