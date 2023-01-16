package com.jike.wlw.service.source.iot;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@ApiModel("阿里云资源")
public class AliyunSource implements Serializable {
    private static final long serialVersionUID = 3984741757845736069L;

    private String accessKey;
    private String accessSecret;
}
