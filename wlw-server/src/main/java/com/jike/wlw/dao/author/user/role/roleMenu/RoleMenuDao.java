package com.jike.wlw.dao.author.user.role.roleMenu;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.AuthFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleMenuDao extends BaseDao {

    public List<PRoleMenu> query(AuthFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, PRoleMenu.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(AuthFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, AuthFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PRoleMenu.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getRoleIdEq())) {
            q.where("o.roleId = :roleIdEq").p("roleIdEq", filter.getRoleIdEq());
        }
        if(!CollectionUtils.isEmpty(filter.getRoleIdIn())){
            q.where("o.roleId in (:roleIdIn)").p("roleIdIn", filter.getRoleIdIn());
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.roleId", "desc");
            }
        } else {
            q.orderBy("o.roleId", "desc");
        }
        return q;
    }

    public void deleteByRoleId(String roleId) {
        String hql = "delete from " + PRoleMenu.TABLE_NAME + "  where roleId = '" + roleId + "'";

        jdbcTemplate.execute(hql);
    }
}
