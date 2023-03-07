package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.serverSubscription.consumerGroup.ali.AliConsumerGroupService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/aliConsumerGroup")
public interface AliConsumerGroupFeignClient extends AliConsumerGroupService {
}
