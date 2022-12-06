/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： chillbaby-wechatweb
 * 文件名： FlowCodeGenerator.java
 * 模块说明：
 * 修改历史：
 * 2018年9月18日 - subinzhu - 创建。
 */
package com.jike.wlw.dao.flowcode;

import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流水DAO
 *
 * @author subinzhu
 */
@Repository
public class FlowCodeDao extends BaseDao {

    public String getMaxCode(String flowName, String prefix) {
        JdbcEntityQuery q = new JdbcEntityQuery("getCode").select("max(o.id)")
            .from(PFlowCode.TABLE_NAME, "o");

        q.where("o.flowName = :flowName").p("flowName", flowName);
        if (!StringUtils.isBlank(prefix)) {
            q.where("o.code like :codeEq").p("codeEq", "%" + prefix + "%");
        }

        List<Object> list = new ArrayList<>();
        list.add(flowName);
        String sql = "select max(o.code) from " + PFlowCode.TABLE_NAME + " o where o.flowName = ? ";
        if (!StringUtils.isBlank(prefix)) {
            sql += " and o.code like ?";
            list.add("%" + prefix + "%");
        }
        List<String> rows = jdbcTemplate.queryForList(sql, list.toArray(), String.class);

        if (CollectionUtils.isEmpty(rows)) {
            return null;
        }
        return rows.get(0);
    }

    @Transactional(value = "bw-inspection-server.txManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void saveCode(String flowName, String code) throws Exception {
        PFlowCode perz = new PFlowCode();
        perz.setFlowName(flowName);
        perz.setCode(code);
        perz.setCreated(new Date());
        save(perz);
    }

}
