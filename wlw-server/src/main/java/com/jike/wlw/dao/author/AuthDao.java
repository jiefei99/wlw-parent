/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： tms-core
 * 文件名： AuthDao.java
 * 模块说明：
 * 修改历史：
 * 2018年4月16日 - subinzhu - 创建。
 */
package com.jike.wlw.dao.author;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.commons.util.converter.ConverterUtil;
import com.geeker123.rumba.jdbc.query.JdbcEntityQuery;
import com.jike.wlw.dao.BaseDao;
import com.jike.wlw.dao.author.user.employee.PEmployee;
import com.jike.wlw.dao.author.user.role.PRole;
import com.jike.wlw.dao.author.user.role.PUserRole;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.user.role.UserRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author subinzhu
 */
@Repository
public class AuthDao extends BaseDao {


    /**
     * 保存用户与角色对应关系
     *
     * @param userId  用户id
     * @param roleIds 角色id列表，not null。
     */
    public void saveUserRole(String userId, List<String> roleIds) throws Exception {
        if (StringUtil.isNullOrBlank(userId)) {
            return;
        }

        // 先删除该用户的所有角色
        String sql = "delete from " + PUserRole.TABLE_NAME + " where userId = '" + userId + "'";
        jdbcTemplate.execute(sql);

        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }

        // 重新插入用户与角色对应关系
        if (roleIds != null && !roleIds.isEmpty()) {
            for (String roleId : roleIds) {
                PUserRole userRole = new PUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                save(userRole);
            }
        }
    }

    /**
     * 根据roleId和userIds删除userRole表
     */
    public void batchRemoveUserRole(String roleId, String userIds) {
        if (StringUtil.isNullOrBlank(roleId) || StringUtil.isNullOrBlank(userIds)) {
            return;
        }
        HashMap<String, Object> userRoleSource = new HashMap<>();
        userRoleSource.put("userIds", userIds);
        userRoleSource.put("roleId", roleId);
        NamedParameterJdbcTemplate namedParameter = new NamedParameterJdbcTemplate(jdbcTemplate);
        String hql = "delete from " + PUserRole.TABLE_NAME + " where roleId =:roleId and userId in (:userIds)";

        namedParameter.update(hql, userRoleSource);
    }

    /**
     * 查询用户与角色对应关系列表
     *
     * @param userId 用户id。为空返回空
     * @return 用户与角色对应关系列表。
     */
    public List<UserRole> getUserRolesByUserId(String userId) throws Exception {
        List<UserRole> result = new ArrayList<>();
        if (!StringUtil.isNullOrBlank(userId)) {
            JdbcEntityQuery q = new JdbcEntityQuery("query").select("distinct *").from(PUserRole.TABLE_NAME, "o").from(
                PRole.TABLE_NAME, "r");
            q.where("o.userId = :userId").p("userId", userId);
            q.where("r.uuid = o.roleId");

            List<PUserRole> userRoles = q.list(jdbcTemplate, PUserRole.class);
            result = ConverterUtil.converts(userRoles, UserRole.class);
        } else {
            JdbcEntityQuery q = new JdbcEntityQuery("query").select("distinct *").from(PRole.TABLE_NAME, "o");

            List<PRole> roles = q.list(jdbcTemplate, PRole.class);
            for (PRole r : roles) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(r.getUuid());
                result.add(userRole);
            }
        }

        return result;
    }

    public List<UserRole> getUserRolesByRoleId(String roleId) throws Exception {
        List<UserRole> result = new ArrayList<>();
        if (!StringUtil.isNullOrBlank(roleId)) {
            JdbcEntityQuery q = new JdbcEntityQuery("query").select("distinct *").from(PUserRole.TABLE_NAME, "o").from(
                PEmployee.TABLE_NAME, "r");
            q.where("o.roleId = :roleId").p("roleId", roleId);
            q.where("r.userId = o.userId");

            List<PUserRole> userRoles = q.list(jdbcTemplate, PUserRole.class);
            result = ConverterUtil.converts(userRoles, UserRole.class);
        } else {
            JdbcEntityQuery q = new JdbcEntityQuery("query").select("distinct *").from(PRole.TABLE_NAME, "o");

            List<PRole> roles = q.list(jdbcTemplate, PRole.class);
            for (PRole r : roles) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(r.getUuid());
                result.add(userRole);
            }
        }

        return result;
    }

    public List<PUserRole> query(AuthFilter filter) {
        JdbcEntityQuery q = getQuery("getQuery", "distinct *", filter);

        return q.list(jdbcTemplate, PUserRole.class, filter.getPage(), filter.getPageSize());
    }

    public long getCount(AuthFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", "count(*)", filter);
        if (q == null) {
            return 0;
        }

        return q.count(jdbcTemplate, PUserRole.class);
    }

    private JdbcEntityQuery getQuery(String name, String select, AuthFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select(select).from(PUserRole.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getRoleIdEq())) {
            q.where("o.roleId = :roleIdEq").p("roleIdEq", filter.getRoleIdEq());
        }
        if (!StringUtil.isNullOrBlank(filter.getUserIdEq())) {
            q.where("o.userId= :userIdEq").p("userIdEq", filter.getUserIdEq());
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.uuid", "desc");
            }
        } else {
            q.orderBy("o.uuid", "desc");
        }

        return q;
    }
}
