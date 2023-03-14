package com.jike.wlw.service.upgrade.ota.dto;

import com.geeker123.rumba.jpa.api.entity.StandardEntity;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageStatusType;
import com.jike.wlw.service.upgrade.ota.OTAUpgradePackageType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @title: OTAUpgradePackageInfoDTO
 * @Author RS
 * @Date: 2023/3/14 15:29
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("升级包信息")
public class OTAUpgradePackageInfoDTO extends StandardEntity {
    private static final long serialVersionUID = 6355133829103467737L;

    public String destVersion;
    public String firmwareDesc;
    public String firmwareId;
    public String firmwareName;
    public String firmwareSign;
    public Integer firmwareSize;
    public String firmwareUrl;
    public String moduleName;
    public List<OTAFirmwareInfoMultiFilesDTO> multiFiles;
    public String productKey;
    public String productName;
    public String signMethod;
    public String srcVersion;
    public OTAUpgradePackageStatusType status;
    public OTAUpgradePackageType type;
    public String udi;
    public Integer verifyProgress;
}


