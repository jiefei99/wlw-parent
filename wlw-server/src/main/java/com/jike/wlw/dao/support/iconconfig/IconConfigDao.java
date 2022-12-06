package com.jike.wlw.dao.support.iconconfig;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.support.iconconfig.IconConfigFilter;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IconConfigDao extends BaseDao {

    public List<PIconConfig> query(IconConfigFilter filter) {
        JdbcEntityQuery q = getQuery("query", "distinct *", filter);
        return q.list(jdbcTemplate, PIconConfig.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(IconConfigFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, IconConfigFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PIconConfig.TABLE_NAME, "o");

        if (filter.getAppIdEq() != null) {
            q.where("o.appId =:appIdEq").p("appIdEq", filter.getAppIdEq().name());
        }
        if (!StringUtil.isNullOrEmpty(filter.getDescriptionLike())) {
            q.where("o.description like (:descriptionLike)").p("descriptionLike", "%" + filter.getDescriptionLike() + "%");
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
