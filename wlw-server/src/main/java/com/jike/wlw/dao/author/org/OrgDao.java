package com.jike.wlw.dao.author.org;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.org.OrgFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Repository
public class OrgDao extends BaseDao {
    public List<POrg> query(OrgFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, POrg.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(OrgFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, OrgFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(POrg.TABLE_NAME, "o");

        if (filter.getOrgTypeEq() != null) {
            q.where("o.orgType = :orgTypeEq").p("orgTypeEq", filter.getOrgTypeEq().name());
        }
        if (filter.getIdLike() != null) {
            q.where("o.id like :idLike").p("idLike", "%" + filter.getIdLike() + "%");
        }
        if (!StringUtil.isNullOrBlank(filter.getUpperIdEq())) {
            q.where("o.upperId = :upperIdEq").p("upperIdEq", filter.getUpperIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        // “批量”Sql语句查询
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.uuid in (:idIn)").p("idIn", filter.getIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getUpperIdIn())) {
            q.where("o.upperId in (:upperIdIn)").p("upperIdIn", filter.getUpperIdIn());
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

    public Integer getMaxId() {
        try {
            String sql = " select max(id) from " + POrg.TABLE_NAME + " o ";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return 10000;
        }
    }
}
