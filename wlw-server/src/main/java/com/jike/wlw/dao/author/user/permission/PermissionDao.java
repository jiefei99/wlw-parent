package com.jike.wlw.dao.author.user.permission;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.user.permission.PermissionFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PermissionDao extends BaseDao {

    public List<PPermission> query(PermissionFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, PPermission.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(PermissionFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, PermissionFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PPermission.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getAppIdEq())) {
            q.where("o.appId = :appIdEq").p("appIdEq", filter.getAppIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.id = :idEq").p("idEq", filter.getIdEq());
        }
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.id in (:idIn)").p("idIn", filter.getIdIn());
        }
        if (!StringUtil.isNullOrBlank(filter.getGroupIdEq())) {
            q.where("o.groupId = :groupIdEq").p("groupIdEq", filter.getGroupIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getRoleIdEq())) {
            q.where("o.roleId = :roleIdEq").p("roleIdEq", filter.getRoleIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.appId", "desc");
            }
        } else {
            q.orderBy("o.appId", "desc");
        }
        return q;
    }

    public void removeByRoleId(String roleId) {
        String hql = "delete from " + PPermission.TABLE_NAME + " where roleId = ?";

        jdbcTemplate.update(hql, roleId);
    }

}
