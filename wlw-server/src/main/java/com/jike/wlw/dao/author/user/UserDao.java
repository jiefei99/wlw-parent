package com.jike.wlw.dao.author.user;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.user.UserFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/19
 * @apiNote
 */
@Repository
public class UserDao extends BaseDao {


    public String insertUser(PUser user) {
        String sql = "insert into " + PUser.TABLE_NAME + " (`creator`,`created`,`modifier`," +
                "`modified`,`tenantId`,`userType`,`name`,`mobile`,`headImage`,`sex`,`status`,`remark`,`uuid`) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(sql, new Object[]{user.getCreator(), user.getCreated(),
                user.getModifier(), user.getModified(), user.getTenantId(), user.getUserType(), user.getName(), user.getMobile(), user.getHeadImage(),
                user.getSex(), user.getStatus(), user.getRemark(), user.getUuid()});

        return user.getUuid();
    }

    public List<PUser> query(UserFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, PUser.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(UserFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, UserFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PUser.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (filter.getUserTypeEq() != null) {
            q.where("o.userType = :userTypeEq").p("userTypeEq", filter.getUserTypeEq().name());
        }
        if (filter.getStatusEq() != null) {
            q.where("o.status = :statusEq").p("statusEq", filter.getStatusEq().name());
        }
        if (!StringUtil.isNullOrBlank(filter.getMobileEq())) {
            q.where("o.mobile = :mobileEq").p("mobileEq", filter.getMobileEq());
        }
        if (!CollectionUtils.isEmpty(filter.getUserIdIn())) {
            q.where("o.uuid in (:userIdIn)").p("userIdIn", filter.getUserIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getMobileIn())) {
            q.where("o.mobile in (:mobilesIn)").p("mobileIn", filter.getMobileIn());
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
