package com.jike.wlw.service.author.org;

import com.geeker123.rumba.commons.paging.AbstractQueryFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author mengchen
 * @date 2022/7/20
 * @apiNote
 */
@Getter
@Setter
@ApiModel("组织查询条件")
public class OrgFilter extends AbstractQueryFilter {

    private static final long serialVersionUID = -180118060460167891L;

    @ApiModelProperty("组织类型等于")
    private OrgType orgTypeEq;
    @ApiModelProperty("上级组织ID等于")
    private String upperIdEq;
    @ApiModelProperty("组织名称等于")
    private String nameEq;
    @ApiModelProperty("标志like")
    private Integer idLike;
    @ApiModelProperty("在组织ID中")
    private List<String> idIn;
    @ApiModelProperty("在上级组织ID中")
    private List<String> upperIdIn;
}
