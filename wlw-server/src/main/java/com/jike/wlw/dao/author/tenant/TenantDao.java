package com.jike.wlw.dao.author.tenant;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.tenant.TenantFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史： 2020/3/11 11:56- sufengjia - 创建。
 */
@Repository
public class TenantDao extends BaseDao {

    public List<PTenant> query(TenantFilter filter) {
        JdbcEntityQuery q = getQuery("distinct *", "query", filter);
        if (q == null) {
            return new ArrayList();
        }
        return q.list(jdbcTemplate, PTenant.class, filter.getPage(), filter.getPageSize());
    }

    public JdbcEntityQuery getQuery(String select, String name, TenantFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PTenant.TABLE_NAME, "o");

        if (filter.getStatus() != null) {
            q.where("o.status = :status").p("status", filter.getStatus());
        }
        if (!CollectionUtils.isEmpty(filter.getIds())) {
            q.where("o.code in (:codes)").p("codes", filter.getIds());
        }
        if (!StringUtil.isNullOrBlank(filter.getName())) {
            q.where("o.name = :name").p("name", filter.getName());
        }

        if (!StringUtil.isNullOrBlank(filter.getKeywords())) {
            q.where("(o.name like :keywords)").p("keywords", "%" + filter.getKeywords() + "%");
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.createdTime", "desc");
            }
        } else {
            q.orderBy("o.createdTime", "desc");
        }

        return q;
    }

    public long getCount(TenantFilter filter) {
        JdbcEntityQuery q = getQuery("count(*)", "getCount", filter);
        return q.count(jdbcTemplate, PTenant.class);
    }
}
