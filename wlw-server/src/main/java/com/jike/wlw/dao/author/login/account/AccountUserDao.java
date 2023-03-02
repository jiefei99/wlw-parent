/**
 * 版权所有©，厦门走云网络科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	chillbaby-web
 * 文件名：	LoginAccount.java
 * 模块说明：
 * 修改历史：
 * 2018年9月18日 - subinzhu - 创建。
 */
package com.jike.wlw.dao.author.login.account;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.login.account.AccountUserFilter;
import com.jike.wlw.service.author.user.UserType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zrs
 */
@Repository
public class AccountUserDao extends BaseDao {

    public PAccountUser getByLoginId(UserType userType, String loginId) throws Exception {
        return get(PAccountUser.class, "userType", userType.name(), "loginId", loginId);
    }

    public List<PAccountUser> query(AccountUserFilter filter) {
        JdbcEntityQuery q = getQuery("query", filter);
        return q.list(jdbcTemplate, PAccountUser.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(AccountUserFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", filter);
        return q.count(jdbcTemplate, PAccountUser.class);
    }

    private JdbcEntityQuery getQuery(String name, AccountUserFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select("*").from(PAccountUser.TABLE_NAME, "o");

        if (StringUtils.isNotBlank(filter.getTenantId())) {
            q.where("o.tenantId = :tenantId").p("tenantId", filter.getTenantId());
        }
        if (filter.getUserType() != null) {
            q.where("o.userType = :userType").p("userType", filter.getUserType().name());
        }
        if (!StringUtil.isNullOrBlank(filter.getUserId())) {
            q.where("o.userId = :userId").p("userId", filter.getUserId());
        }
        if (!CollectionUtils.isEmpty(filter.getUserIds())) {
            q.where("o.userId in (:userIds)").p("userIds", filter.getUserIds());
        }
        if (filter.getState() != null) {
            q.where("o.state = :state").p("state", filter.getState().name());
        }
        if (!StringUtil.isNullOrBlank(filter.getLoginId())) {
            q.where("o.loginId = :loginId").p("unionId", filter.getLoginId());
        }
        q.where("o.isDeleted = 0");

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
