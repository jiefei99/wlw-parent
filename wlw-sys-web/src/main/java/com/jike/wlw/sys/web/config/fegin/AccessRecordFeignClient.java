package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.management.access.AccessRecordService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class)
public interface AccessRecordFeignClient extends AccessRecordService {
}
