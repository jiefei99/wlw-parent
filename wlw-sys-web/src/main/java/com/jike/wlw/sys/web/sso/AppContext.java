/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 11:10 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.sso;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 应用上下文
 */
@Getter
@Setter
@ApiModel("应用上下文")
public class AppContext implements Serializable {
    private static final long serialVersionUID = 5133224668268548239L;

    private static final ThreadLocal<AppContext> holder = new ThreadLocal();

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("TOKEN")
    private String token;

    public static void setContext(AppContext context) {
        holder.set(context);
    }

    public static AppContext getContext() {
        AppContext context = holder.get();
        return context == null ? new AppContext() : holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}