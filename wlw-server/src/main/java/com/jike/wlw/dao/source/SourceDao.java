package com.jike.wlw.dao.source;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.source.SourceFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class SourceDao extends BaseDao {

    public List<PSource> query(SourceFilter filter) {
        JdbcEntityQuery q = getQuery("query", "*", filter);
        return q.list(jdbcTemplate, PSource.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(SourceFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        return q.count(jdbcTemplate, String.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, SourceFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PSource.TABLE_NAME, "o");

        //eq查询
        if (StringUtils.isNotBlank(filter.getKeywords())) {
            q.where("(o.name like :keywords)").p("keywords", "%" + filter.getKeywords() + "%");
        }
        if (StringUtils.isNotBlank(filter.getTenantIdEq())) {
            q.where("o.tenantId = :tenantIdEq").p("tenantIdEq", filter.getTenantIdEq());
        }
        if (StringUtils.isNotBlank(filter.getUuidEq())) {
            q.where("o.uuid = :uuidEq").p("uuidEq", filter.getUuidEq());
        }
        if (StringUtils.isNotBlank(filter.getNameEq())) {
            q.where("o.name = :nameEq").p("nameEq", filter.getNameEq());
        }
        if (filter.getEnvironmentEq() != null) {
            q.where("o.environment = :environmentEq").p("environmentEq", filter.getEnvironmentEq().name());
        }
        if (filter.getTypeEq() != null) {
            q.where("o.type = :typeEq").p("typeEq", filter.getTypeEq().name());
        }
        if (filter.getStatusEq() != null) {
            q.where("o.status = :statusEq").p("statusEq", filter.getStatusEq().name());
        }
        if (filter.getDeletedEq() != null) {
            q.where("o.deleted = :deletedEq").p("deletedEq", filter.getDeletedEq());
        }
        //in查询
        if (!CollectionUtils.isEmpty(filter.getUuidIn())) {
            q.where("o.uuid in  (:uuidIn)").p("uuidIn", filter.getUuidIn());
        }
        if (!CollectionUtils.isEmpty(filter.getNameIn())) {
            q.where("o.name in  (:nameIn)").p("nameIn", filter.getNameIn());
        }
        if (!CollectionUtils.isEmpty(filter.getEnvironmentIn())) {
            q.where("o.environment in  (:environmentIn)").p("environmentIn", filter.getEnvironmentIn());
        }
        if (!CollectionUtils.isEmpty(filter.getTypeIn())) {
            q.where("o.type in  (:typeIn)").p("typeIn", filter.getTypeIn());
        }
        if (!CollectionUtils.isEmpty(filter.getStatusIn())) {
            q.where("o.status in  (:statusIn)").p("statusIn", filter.getStatusIn());
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
