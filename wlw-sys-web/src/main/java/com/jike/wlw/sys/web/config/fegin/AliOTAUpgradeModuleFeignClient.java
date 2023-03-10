package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradeModuleService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/aliOTAUpgradeModule")
public interface AliOTAUpgradeModuleFeignClient extends AliOTAUpgradeModuleService {
}
