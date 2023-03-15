package com.jike.wlw.sys.web.config.fegin;

import com.jike.bsc.commons.service.config.ConfigService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bsc-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/config")
public interface ConfigFeignClient extends ConfigService {
}
