package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.upgrade.ota.ali.AliOTAUpgradePackageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/aliOTAUpgradePackage")
public interface AliOTAUpgradePackageFeignClient extends AliOTAUpgradePackageService {
}
