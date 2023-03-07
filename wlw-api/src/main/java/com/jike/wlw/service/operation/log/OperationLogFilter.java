package com.jike.wlw.service.operation.log;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/6
 * @apiNote
 */
@Getter
@Setter
@ApiModel("操作日志查询条件")
public class OperationLogFilter extends AbstractQueryFilter {

    private static final long serialVersionUID = 8433794516529058115L;

    @ApiModelProperty("信息块")
    private String parts;

    @ApiModelProperty("操作日志ID")
    private String idEq;
    @ApiModelProperty("操作类型")
    private OperationType type;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("关联ID")
    private String relationId;
    @ApiModelProperty("操作内容类似于")
    private String contentLike;
    @ApiModelProperty("操作备注类似于")
    private String remakeLike;

    @ApiModelProperty("在操作日志ID中")
    private List<String> idIn;
    @ApiModelProperty("在操作类型列表中")
    private List<OperationType> typeIn;
    @ApiModelProperty("在用户id中")
    private List<String> userIdIn;
    @ApiModelProperty("在关联ID中")
    private List<String> relationIdIn;


}
