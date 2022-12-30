package com.jike.wlw.service.physicalmodel;

import com.geeker123.rumba.jpa.api.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("物模型")
public class PhysicalModel extends Entity {
    private static final long serialVersionUID = 130312029720547601L;

    @ApiModelProperty("物模型编号")
    private String id;
    @ApiModelProperty("物模型名称")
    private String name;
    @ApiModelProperty("功能编号集合")
    private List<String> functionIds;

}
