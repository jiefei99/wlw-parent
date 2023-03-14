package com.jike.wlw.service.upgrade.ota.dto;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @title: OTAFirmwareInfoMultiFilesDTO
 * @Author RS
 * @Date: 2023/3/14 15:33
 * @Version 1.0
 */

@Setter
@Getter
@ApiModel("升级包信息")
public class OTAFirmwareInfoMultiFilesDTO extends StandardEntity {
    private static final long serialVersionUID = 6355133829103464737L;

    public String fileMd5;
    public String name;
    public String signValue;
    public Integer size;
    public String url;
}


