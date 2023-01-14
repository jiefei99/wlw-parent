package com.jike.wlw.dao.author.user.employee;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.dao.author.user.PUser;
import com.jike.wlw.service.author.user.employee.EmployeeFilter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/19
 * @apiNote
 */
@Repository
public class EmployeeDao extends BaseDao {

    public List<PEmployee> query(EmployeeFilter filter) {
        JdbcEntityQuery q = getQuery("getQuery", "distinct *", filter);
        return q.list(jdbcTemplate, PEmployee.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(EmployeeFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, PEmployee.class);
    }

    public JdbcEntityQuery getQuery(String name, String select, EmployeeFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PEmployee.TABLE_NAME, "o");

//        if (!StringUtil.isNullOrBlank(filter.getParts())) {
//            q.where("o.parts = :parts").p("parts", filter.getParts());
//        }
        if (!StringUtil.isNullOrBlank(filter.getKeywords())) {
            q.where("(o.userId in (select u.uuid from " + PUser.TABLE_NAME + " u where u.name like :keywords or u.mobile like :keywords))")
                    .p("keywords", "%" + filter.getKeywords() + "%");
        }
        if (!StringUtil.isNullOrBlank(filter.getUserIdEq())) {
            q.where("o.userId = :userIdEq").p("userIdEq", filter.getUserIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.id = :idEq").p("idEq", filter.getIdEq());
        }
        if (filter.getAdminEq() != null) {
            q.where("o.admin = :adminEq").p("adminEq", filter.getAdminEq());
        }
        if (!CollectionUtils.isEmpty(filter.getTenentIdIn())) {
            q.where("o.tenantId in (:tenantIdIn)").p("tenantIdIn", filter.getTenentIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.id in (:idIn)").p("idIn", filter.getIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getUserIdIn())) {
            q.where("o.userId in (:userIdIn)").p("userIdIn", filter.getUserIdIn());
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

    public PEmployee getMaxId(String tenantId) {
        String sql = " select * from " + PEmployee.TABLE_NAME + " o where o.tenantId = ?  order by o.id desc limit 1";
        List<PEmployee> list = jdbcTemplate.query(sql, new Object[]{tenantId}, new BeanPropertyRowMapper<>(PEmployee.class));
        return list.isEmpty() ? null : list.get(0);
    }

}
