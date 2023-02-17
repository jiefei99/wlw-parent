package com.jike.wlw.dao.physicalmodel;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PStandardEntity;
import com.jike.wlw.service.physicalmodel.CallType;
import com.jike.wlw.service.physicalmodel.EventType;
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

    public static final String TABLE_NAME = "wlw_model_device_function";

    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("物模型Id")
    private String modelDeviceId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("功能类型")
    private String type;
    @ApiModelProperty("事件类型")
    private EventType eventType;
    @ApiModelProperty("标识符")
    private String identifier;
    @ApiModelProperty("读写类型")
    private String rwFlag;
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("服务调用类型")
    private CallType callType;
    @ApiModelProperty("描述")
    private String details;
    @ApiModelProperty("是否是标准功能的必选事件")
    private boolean required;
    @ApiModelProperty("逻辑删除")
    private boolean isDeleted;
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}


