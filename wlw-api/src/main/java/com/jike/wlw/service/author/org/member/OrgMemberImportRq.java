package com.jike.wlw.service.author.org.member;

import com.jike.wlw.service.author.org.OrgType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@ApiModel("批量导入条件")
public class OrgMemberImportRq implements Serializable {
    private static final long serialVersionUID = -4937457707213180830L;

    @ApiModelProperty("文件id")
    private String filePath;
    @ApiModelProperty("组织ID")
    private String orgId;
    @ApiModelProperty("组织类型")
    private OrgType orgType;
    @ApiModelProperty("成员工号")
    private String number;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("姓名")
    private String name;
}
