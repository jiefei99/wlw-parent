/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/14 20:26 - chenpeisi - 创建。
 */
package com.jike.wlw.dao.author.user.credentials.account.pwd;

import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.service.author.user.credentials.account.pwd.PwdAccountFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 密码账户DAO
 */
@Repository
public class PwdAccountDao extends BaseDao {

    public List<PPwdAccount> query(PwdAccountFilter filter) {
        JdbcEntityQuery q = getQuery("query", filter);
        return q.list(jdbcTemplate, PPwdAccount.class, filter.getPage(), filter.getPageSize());
    }

    private JdbcEntityQuery getQuery(String name, PwdAccountFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select("distinct *").from(PPwdAccount.TABLE_NAME, "o");

        if (filter.getUserTypeEq() != null) {
            q.where("o.userType = :userTypeEq").p("userTypeEq", filter.getUserTypeEq().name());
        }
        if (!StringUtil.isNullOrBlank(filter.getUserIdEq())) {
            q.where("o.userId = :userIdEq").p("userIdEq", filter.getUserIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getLoginIdEq())) {
            q.where("o.loginId = :loginId").p("loginId", filter.getLoginIdEq());
        }
        if (!CollectionUtils.isEmpty(filter.getUserIdIn())) {
            q.where("o.userId in (:userIdIn)").p("userIdIn", filter.getUserIdIn());
        }

        return q;
    }
}