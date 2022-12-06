/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/14 20:23 - chenpeisi - 创建。
 */
package com.jike.wlw.dao.author.user.credentials.account.pwd;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码账户
 */
@Getter
@Setter
public class PPwdAccount extends PEntity implements JdbcEntity {
    private static final long serialVersionUID = -7913120184291161330L;

    public static final String TABLE_NAME = "wlw_pwd_account";

    private String userType;
    private String userId;
    private String remark;

    private String loginId;
    private String password;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}