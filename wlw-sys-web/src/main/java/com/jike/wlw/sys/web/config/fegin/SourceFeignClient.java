package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.source.SourceService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/source")
public interface SourceFeignClient extends SourceService {
}
