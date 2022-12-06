/**
 * 
 */
package com.jike.wlw.dao;

import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.jdbc.JdbcEntityManager;
import com.geeker123.rumba.jdbc.entity.HashId;
import com.geeker123.rumba.jdbc.entity.HashUuid;
import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ShadowX
 *
 */
public abstract class BaseDao {

  @Autowired
  protected JdbcTemplate jdbcTemplate;

  protected String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  protected synchronized void allocateId(HashId entity) {
    if (StringUtil.isNullOrBlank(entity.getId())) {
      entity.setId(generateUUID());
    }
  }

  public <T extends HashUuid & JdbcEntity> void save(T entity) throws Exception {
    getJdbcEntityManager().save(entity);
  }

  public <T extends HashUuid & JdbcEntity> void save(List<T> entitys) throws Exception {
    getJdbcEntityManager().save(entitys);
  }

  public <T extends HashUuid & JdbcEntity> T get(Class<T> clazz, String uuid) throws Exception {
    return getJdbcEntityManager().get(clazz,uuid);
  }

  public <T extends HashUuid & JdbcEntity> T get(Class<T> clazz, Object fieldName, Object arg,
      Object... fieldNamesAndArgs) throws Exception {
    List<T> list = getList(clazz, fieldName, arg, fieldNamesAndArgs);

    return CollectionUtils.isEmpty(list) ? null : list.get(0);
  }

  public <T extends HashUuid & JdbcEntity> List<T> getList(Class<T> clazz, Object fieldName,
      Object arg, Object... fieldNamesAndArgs) throws Exception {
    List<String> fieldNames = new ArrayList<String>();
    List<Object> args = new ArrayList<Object>();

    fieldNames.add(fieldName.toString());
    args.add(arg);

    int i = 0;
    for (Object o : fieldNamesAndArgs) {
      if (i % 2 == 0) {
        fieldNames.add(o.toString());
      } else {
        args.add(o.toString());
      }
      i++;
    }

    return getJdbcEntityManager().getList(clazz, fieldNames.toArray(new String[] {}),
        args.toArray(new Object[] {}));
  }

  public <T extends HashUuid & JdbcEntity> void remove(T entity) {
    getJdbcEntityManager().remove(entity);
  }

  public <T extends HashUuid & JdbcEntity> void remove(Class<T> clazz, String uuid)
      throws Exception {
    getJdbcEntityManager().remove(clazz,uuid);
  }

  private JdbcEntityManager jdbcEntityManager;

  private JdbcEntityManager getJdbcEntityManager() {
    if (jdbcEntityManager == null) {
      jdbcEntityManager = new JdbcEntityManager(jdbcTemplate);
    }
    return jdbcEntityManager;
  }
}
