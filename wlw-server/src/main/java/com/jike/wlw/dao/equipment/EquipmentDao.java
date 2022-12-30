package com.jike.wlw.dao.equipment;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.equipment.EquipmentFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class EquipmentDao extends BaseDao {

    public List<PEquipment> query(EquipmentFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PEquipment.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(EquipmentFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, EquipmentFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PEquipment.TABLE_NAME, "o");

        //eq查询
        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.id = :id").p("id", filter.getIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getProductKeyEq())) {
            q.where("o.productKey = :productKeyEq").p("productKeyEq", filter.getProductKeyEq());
        }
        if (filter.getStatusEq() != null) {
            q.where("o.status = :statusEq").p("statusEq", filter.getStatusEq().name());
        }
        //in查询
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.id in  (:ids)").p("ids", filter.getIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getProductKeyIn())) {
            q.where("o.productKey in  (:productKeyIn)").p("productKeyIn", filter.getProductKeyIn());
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
