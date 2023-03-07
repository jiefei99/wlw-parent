package com.jike.wlw.dao.operation.log;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.operation.log.OperationLogFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Repository
public class OperationLogDao extends BaseDao {

    public List<POperationLog> query(OperationLogFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, POperationLog.class, filter.getPage(), filter.getPageSize());
    }

    private JdbcEntityQuery getQuery(String name, String select, OperationLogFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(POperationLog.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getIdEq())) {
            q.where("o.uuid = :idEq").p("id", filter.getIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getRelationId())) {
            q.where("o.relationId = :relationId").p("relationId", filter.getRelationId());
        }
        if (filter.getType() != null) {
            q.where("o.type = :type").p("type", filter.getType().name());
        }
        // “in”批量查询
        if (!CollectionUtils.isEmpty(filter.getIdIn())) {
            q.where("o.uuid in (:idIn)").p("idIn", filter.getIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getUserIdIn())) {
            q.where("o.userIdIn in (:userIdIn)").p("userIdIn", filter.getUserIdIn());
        }
        if (!CollectionUtils.isEmpty(filter.getTypeIn())) {
            q.where("o.typeIn in (:typeIn)").p("typeIn", filter.getTypeIn());
        }
        if (!CollectionUtils.isEmpty(filter.getRelationIdIn())) {
            q.where("o.relationIdIn in (:relationIdIn)").p("relationIdIn", filter.getRelationIdIn());
        }
        // “like”模糊查询
        if (!StringUtil.isNullOrBlank(filter.getContentLike())) {
            q.where(" o.content like :content ").p("contentlike", "%" + filter.getContentLike() + "%");
        }
        if (!StringUtil.isNullOrBlank(filter.getContentLike())) {
            q.where(" o.remake like :remake ").p("remakelike", "%" + filter.getRemakeLike() + "%");
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

    public long getCount(OperationLogFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, POperationLog.class);
    }
}
