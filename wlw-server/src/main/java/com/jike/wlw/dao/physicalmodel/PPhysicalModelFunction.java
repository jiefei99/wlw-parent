package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.DataType;
import com.jike.wlw.service.physicalmodel.EventType;
import com.jike.wlw.service.physicalmodel.ThingModelJsonType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Cache;

/**
 * @title: PPhysicalModelService
 * @Author RS
 * @Date: 2023/2/16 17:37
 * @Version 1.0
 */

@Getter
@Setter
public class PPhysicalModelFunction extends PStandardEntity implements JdbcEntity {
    private static final long serialVersionUID = -6747778097141439297L;

    public static final String TABLE_NAME = "wlw_physical_model_function";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("物模型Id")
    private String modelModuleId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("功能类型")
    private ThingModelJsonType type;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否必填 （阿里数据结构中的字段）")
    private boolean required;
    @ApiModelProperty("是否删除")
    private int isDeleted;
    //属性
    @ApiModelProperty("读写类型")
    private String rwFlag;
    @ApiModelProperty("数据类型")
    private DataType dataType;

    //事件
    @ApiModelProperty("事件类型")
    private EventType eventType;

    //服务
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("服务调用类型")
    private CallType callType;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


