package com.jike.wlw.dao.config;

import com.jike.wlw.dao.BaseDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigDao extends BaseDao {

    public List<PConfig> getByGroup(String group) {
        String sql = "SELECT * FROM " + PConfig.TABLE_NAME + " o WHERE o.configGroup = '" + group + "'";
        List<PConfig> list = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<PConfig>(PConfig.class));

        return list;
    }

    public PConfig getConfig(String group, String key) {
        String sql = "SELECT * FROM " + PConfig.TABLE_NAME + " o WHERE o.configGroup = '" + group + "' AND o.configKey ='" + key + "'";
        List<PConfig> list = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<PConfig>(PConfig.class));

        return list.isEmpty() ? null : list.get(0);
    }

    public void deleteByGroup(String group) {
        String hql = "delete from " + PConfig.TABLE_NAME + "  where configGroup = " + group;

        jdbcTemplate.execute(hql);
    }
}
