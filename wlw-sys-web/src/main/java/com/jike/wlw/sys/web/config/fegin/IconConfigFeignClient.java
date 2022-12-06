package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.support.iconconfig.IconConfigService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class)
public interface IconConfigFeignClient extends IconConfigService {
}
