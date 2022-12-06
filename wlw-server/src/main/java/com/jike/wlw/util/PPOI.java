package com.jike.wlw.util;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("标记地址")
public class PPOI implements Serializable {
    private static final long serialVersionUID = 971307273376920290L;

    private BigDecimal lng;
    private BigDecimal lat;
    private String provinceCode;
    private String province;
    private String cityCode;
    private String city;
    private String districtCode;
    private String district;
    private String township;
    private String poiName;
    private String poiAddress;
    private String address;

}