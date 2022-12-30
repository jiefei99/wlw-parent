package com.jike.wlw.service.product;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("产品信息")
public class Product extends StandardEntity {
    private static final long serialVersionUID = 6355685899103067786L;

    //三元组
//    @ApiModelProperty("编号") //deviceName：可以自定义，否则自动生成
//    private String id;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("产品secret")
    private String productSecret;
    @ApiModelProperty("物模型编号集合")
    private List<String> physicalModelIds;

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("备注")
    private String remark;

    //所属品类（标准/自定义：类似于预设型号，在创建设备的时候会额外提供一个点位模板）、节点类型（直连设备/网关子设备/网关设备）、数据格式（默认alink协议，格式json）
    //productKey、productSecret：是否用id即可？

}
