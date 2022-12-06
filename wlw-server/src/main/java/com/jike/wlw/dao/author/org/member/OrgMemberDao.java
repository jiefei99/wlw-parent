package com.jike.wlw.dao.author.org.member;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.org.member.OrgMemberFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Repository
public class OrgMemberDao extends BaseDao {
    public List<POrgMember> query(OrgMemberFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, POrgMember.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(OrgMemberFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, OrgMemberFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(POrgMember.TABLE_NAME, "o");

        //like 模糊查询
        if (!StringUtil.isNullOrBlank(filter.getKeywords())) {
            q.where("(o.name like :keywords or o.mobile like :keywords)").p("keywords", "%" + filter.getKeywords() + "%");
        }

        if (!StringUtil.isNullOrBlank(filter.getUuidEq())) {
            q.where("o.uuid = :uuidEq").p("uuidEq", filter.getUuidEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getUuidEq())) {
            q.where("o.uuid = :uuidEq").p("uuidEq", filter.getUuidEq());
        }
        if (filter.getIsAdmin() != null) {
            q.where("o.isAdmin = :isAdmin").p("isAdmin", filter.getIsAdmin());
        }
        if (filter.getStatus() != null) {
            q.where("o.status = :status").p("status", filter.getStatus().name());
        }
        if (filter.getOrgTypeEq() != null) {
            q.where("o.orgType = :orgTypeEq").p("orgTypeEq", filter.getOrgTypeEq().name());
        }
        if (!StringUtil.isNullOrBlank(filter.getOrgIdEq())) {
            q.where("o.orgId = :orgIdEq").p("orgIdEq", filter.getOrgIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNumberEq())) {
            q.where("o.number = :numberEq").p("numberEq", filter.getNumberEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getUserIdEq())) {
            q.where("o.userId = :userIdEq").p("userIdEq", filter.getUserIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getMobileEq())) {
            q.where("o.mobile = :mobileEq").p("mobileEq", filter.getMobileEq());
        }
        if (filter.getIdLike() != null) {
            q.where("o.id like :idLike").p("idLike", "%" + filter.getIdLike() + "%");
        }
        if (!StringUtil.isNullOrBlank(filter.getNameLike())) {
            q.where("o.name like :nameLike").p("nameLike", "%" + filter.getNameLike() + "%");
        }
        if (!CollectionUtils.isEmpty(filter.getUserIdIn())) {
            q.where("o.userId in (:userIdIn)").p("userIdIn", filter.getUserIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getOrgIdIn())) {
            q.where("o.orgId in (:orgIdIn)").p("orgIdIn", filter.getOrgIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getUuidIn())) {
            q.where("o.uuid in (:uuidIn)").p("uuidIn", filter.getUuidIn());
        }
        if (!CollectionUtils.isEmpty(filter.getNumberIn())) {
            q.where("o.number in (:numberIn)").p("numberIn", filter.getNumberIn());
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
            String sql = " select max(id) from " + POrgMember.TABLE_NAME + " o ";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return 10000;
        }
    }
}
