/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： chillbaby-web
 * 文件名： FlowCodeGenerator.java
 * 模块说明：
 * 修改历史：
 * 2018年9月18日 - subinzhu - 创建。
 */
package com.jike.wlw.dao.management.access;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.management.access.AccessRecordFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class AccessRecordDao extends BaseDao {

    public List<PAccessRecord> query(AccessRecordFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PAccessRecord.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(AccessRecordFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, AccessRecordFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select)
                .from(PAccessRecord.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getIpEq())) {
            q.where(" o.ip = :ipEq").p("ipEq", filter.getIpEq());
        }
        if (filter.getStatusEq() != null) {
            q.where(" o.status = :statusEq").p("statusEq", filter.getStatusEq().name());
        }
        if (!CollectionUtils.isEmpty(filter.getStatusIn())) {
            q.where(" o.status in (:statusIn)").p("statusIn", filter.getStatusIn());
        }

        if (!StringUtil.isNullOrBlank(filter.getActionEq())) {
            q.where(" o.action = :actionEq").p("actionEq", filter.getActionEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getLoginUserIdEq())) {
            q.where(" o.loginUserId = :loginUserIdEq")
                    .p("loginUserIdEq", filter.getLoginUserIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getLoginUserNameLike())) {
            q.where(" o.loginUserName like :loginUserNameLike")
                    .p("loginUserNameLike", "%" + filter.getLoginUserNameLike() + "%");
        }
        if (!CollectionUtils.isEmpty(filter.getLoginUserIdIn())) {
            q.where(" o.loginUserId in  (:loginUserIdIn)")
                    .p("loginUserIdIn", filter.getLoginUserIdIn());
        }

        if (filter.getCreatedGte() != null) {
            q.where(" o.created >= createdGte")
                    .p("createdGte", filter.getCreatedGte());
        }
        if (filter.getCreatedLte() != null) {
            q.where(" o.created <= createdLte")
                    .p("createdLte", filter.getCreatedLte());
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
