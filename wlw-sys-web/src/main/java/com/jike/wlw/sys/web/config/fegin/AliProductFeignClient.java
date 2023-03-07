package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.product.info.ali.AliProductService;
import com.jike.wlw.service.serverSubscription.consumerGroup.ali.AliConsumerGroupService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/product")
public interface AliProductFeignClient extends AliProductService {
}
