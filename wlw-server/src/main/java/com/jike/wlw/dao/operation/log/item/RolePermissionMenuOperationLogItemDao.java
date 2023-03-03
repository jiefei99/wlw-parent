package com.jike.wlw.dao.operation.log.item;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.operation.log.item.RolePermissionMenuOperationLogItemFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 14:55- zhengzhoudong - 创建。
 */
@Repository
public class RolePermissionMenuOperationLogItemDao extends BaseDao {

    public List<PRolePermissionMenuOperationLogItem> query(RolePermissionMenuOperationLogItemFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PRolePermissionMenuOperationLogItem.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(RolePermissionMenuOperationLogItemFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, PRolePermissionMenuOperationLogItem.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, RolePermissionMenuOperationLogItemFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PRolePermissionMenuOperationLogItem.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getOperationLogIdEq())) {
            q.where("o.operationLogId = :operationLogIdEq").p("operationLogIdEq", filter.getOperationLogIdEq());
        }
        // “in”批量查询
        if (!CollectionUtils.isEmpty(filter.getOperationLogIdIn())) {
            q.where("o.operationLogId in (:operationLogIdIn)").p("operationLogIdIn", filter.getOperationLogIdIn());
        }
        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.operationLogId", "desc");
            }
        } else {
            q.orderBy("o.operationLogId", "desc");
        }

        return q;
    }

}
