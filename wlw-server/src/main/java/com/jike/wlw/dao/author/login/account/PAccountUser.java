/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名：	chillbaby-web
 * 文件名：	LoginAccount.java
 * 模块说明：
 * 修改历史：
 * 2018年7月12日 - chenpeisi - 创建。
 */
package com.jike.wlw.dao.author.login.account;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.common.FreezeState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录账号|接口对象
 *
 * @author zrs
 */
@Getter
@Setter
public class PAccountUser extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -323870501267400567L;

    public static final String TABLE_NAME = "wlw_account_user";

    @ApiModelProperty("用户类型")
    private String tenantId;
    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("用户头像")
    private String headImage;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("账号状态")
    private String state = FreezeState.NORMAL.name();

    @ApiModelProperty("登录账号")
    private String loginId;
    @ApiModelProperty("登录密码")
    private String password;
    @ApiModelProperty("是否删除")
    private boolean isDeleted;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
