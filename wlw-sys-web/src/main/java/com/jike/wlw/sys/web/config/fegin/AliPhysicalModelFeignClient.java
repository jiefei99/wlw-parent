package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.physicalmodel.ali.AliPhysicalModelManagerService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wza
 * @create 2023/3/17
 */
@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/aliPhysicalModel")
public interface AliPhysicalModelFeignClient extends AliPhysicalModelManagerService {
}
