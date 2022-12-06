package com.jike.wlw.service.message;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import com.jike.wlw.service.author.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author ZhengZhouDong
 * @Date 2020/5/14 16:54
 */
@Setter
@Getter
@ApiModel("消息查询条件")
public class MessageFilter extends AbstractQueryFilter {
    private static final long serialVersionUID = -8451614235807643536L;

    @ApiModelProperty("用户ID等于")
    private String userIdEq;
    @ApiModelProperty("用户类型等于")
    private UserType userTypeEq;
    @ApiModelProperty("业务ID等于")
    private String businessIdEq;
    @ApiModelProperty("消息类型")
    private List<String> messageType;
    @ApiModelProperty("消息是否已读")
    private Boolean hasRead;

}
