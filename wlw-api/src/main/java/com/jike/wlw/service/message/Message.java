package com.jike.wlw.service.message;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ZhengZhouDong
 * @Date 2020/5/14 16:30
 */
@Setter
@Getter
@ApiModel("消息")
public class Message extends StandardEntity {
    private static final long serialVersionUID = 2403912816585853912L;

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("业务ID")
    private String businessId;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("是否已读")
    private boolean hasRead;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("类型")
    private MessageType messageType;
    @ApiModelProperty("图片")
    private String image;
    @ApiModelProperty("备注")
    private String remark;

}
