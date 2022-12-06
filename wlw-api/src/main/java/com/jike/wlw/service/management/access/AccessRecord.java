package com.jike.wlw.service.management.access;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRecord extends StandardEntity {
    private static final long serialVersionUID = 3868485440752301607L;

    @ApiModelProperty("ip")
    private String ip;
    @ApiModelProperty("浏览记录状态")
    private AccessRecordStatus status;
    @ApiModelProperty("动作")
    private String action;
    @ApiModelProperty("登陆的用户ID")
    private String loginUserId;
    @ApiModelProperty("登陆的用户名")
    private String loginUserName;

}
