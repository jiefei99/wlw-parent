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
import com.jike.wlw.dao.author.user.role.PRoleMenu;
import com.jike.wlw.dao.author.user.role.PUserRole;
import com.jike.wlw.service.author.AuthFilter;
import com.jike.wlw.service.author.user.role.RoleFilter;
import com.jike.wlw.service.author.user.role.UserRole;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
     * 删除角色
     *
     * @param roleUuid 角色ID
     */
    public void removeRole(String roleUuid, String tenantId) {
        if (StringUtil.isNullOrBlank(roleUuid)) {
            return;
        }

        // 删除角色与菜单对应关系
        String sql = "delete from " + PRoleMenu.TABLE_NAME + " where roleUuid = ?";
        jdbcTemplate.update(sql, roleUuid);

        // 删除用户与角色对应关系
        sql = "delete from " + PUserRole.TABLE_NAME + "  where roleUuid = ?";
        jdbcTemplate.update(sql, roleUuid);

        // 删除角色
        sql = "delete from " + PRole.TABLE_NAME + " where uuid = ? and tenantId = ?";
        jdbcTemplate.update(sql, roleUuid, tenantId);

    }

    /**
     * 根据roleId和userIds删除userRole表
     */
    public void batchRemoveUserRole(String roleId, List<String> userIds) {
        if (StringUtil.isNullOrBlank(roleId) || CollectionUtils.isEmpty(userIds)) {
            return;
        }
        HashMap<String, Object> userRoleSource = new HashMap<>();
        userRoleSource.put("userIds", userIds);
        userRoleSource.put("roleId", roleId);
        NamedParameterJdbcTemplate namedParameter = new NamedParameterJdbcTemplate(jdbcTemplate);
        String hql = "delete from " + PUserRole.TABLE_NAME + " where roleUuid =:roleId and userUuid in (:userIds)";

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

    /**
     * 查询用户与角色对应关系列表
     *
     * @param userId 用户uuid。为空返回空
     * @return 用户与角色对应关系列表。
     */
    public List<UserRole> getUserRoles(String tenantId, String userId) {
        String sql = null;

        List<UserRole> result = new ArrayList<>();
        if (!StringUtil.isNullOrBlank(userId)) {
            sql = "select o.*,r.name as roleName from " + PUserRole.TABLE_NAME + " o left Join " + PRole.TABLE_NAME + " r on r.uuid = o.roleId and r.tenantId = o.tenantId where r.tenantId = ?" + " having o.userId = ? ";

            result = jdbcTemplate.query(sql, new Object[]{tenantId, userId}, new BeanPropertyRowMapper<>(UserRole.class));
        } else {
            sql = "select * from " + PRole.TABLE_NAME;

            List<PRole> roles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PRole.class));
            if (!CollectionUtils.isEmpty(roles)) {
                for (PRole role : roles) {
                    UserRole userRole = new UserRole();
                    userRole.setRoleId(role.getUuid());
                    result.add(userRole);
                }
            }
        }

        return result;
    }

    /**
     * 根据roleIds和userId删除userRole表
     */
    public void batchRemoveUserRoles(String userId, List<String> roleIds) {
        if (StringUtil.isNullOrBlank(userId) || CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        HashMap<String, Object> userRoleSource = new HashMap<>();
        userRoleSource.put("userId", userId);
        userRoleSource.put("roleIds", roleIds);
        NamedParameterJdbcTemplate namedParameter = new NamedParameterJdbcTemplate(jdbcTemplate);
        String hql = "delete from " + PUserRole.TABLE_NAME + " where userUuid =:userId and roleUuid in (:roleIds)";

        namedParameter.update(hql, userRoleSource);
    }

    /**
     * 查询角色列表
     *
     * @param filter 查询过滤器，not null。
     * @return 查询结果
     */
    public List<PRole> queryRole(RoleFilter filter) {
        JdbcEntityQuery q = getQuery("query", filter);
        return q.list(jdbcTemplate, PRole.class, filter.getPage(), filter.getPageSize());
    }

    /**
     * 查询角色记录总数
     *
     * @param filter 过滤条件
     * @return 记录总数
     */
    public long getRoleCount(RoleFilter filter) {
        JdbcEntityQuery q = getQuery("getCount", filter);
        return q.count(jdbcTemplate, PRole.class);
    }

    private JdbcEntityQuery getQuery(String name, RoleFilter filter) {
        JdbcEntityQuery q = new JdbcEntityQuery(name).select("*").from(PRole.TABLE_NAME, "o");

        if (!StringUtil.isNullOrBlank(filter.getKeywords())) {
            q.where("(o.name like :keywords or o.remark like :keywords)").p("keywords", "%" + filter.getKeywords() + "%");
        }
        if (!CollectionUtils.isEmpty(filter.getUuidIn())) {
            q.where("o.uuid in (:uuidIn)").p("uuidIn", filter.getUuidIn());
        }

        if (filter.getOrders() != null && !filter.getOrders().isEmpty()) {
            for (AbstractQueryFilter.Order order : filter.getOrders()) {
                if (order != null && !StringUtil.isNullOrBlank(order.getSortKey())) {
                    q.orderBy("o." + order.getSortKey(), order.isDesc() ? "desc" : "asc");
                }
            }
            if (q.getOrderBys().isEmpty()) {
                q.orderBy("o.name", "asc");
            }
        } else {
            q.orderBy("o.name", "asc");
        }

        return q;
    }
}
