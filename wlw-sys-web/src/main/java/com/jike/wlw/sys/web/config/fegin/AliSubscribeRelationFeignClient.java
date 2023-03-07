package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.serverSubscription.subscribe.ali.AliSubscribeRelationService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/subscribeRelation")
public interface AliSubscribeRelationFeignClient extends AliSubscribeRelationService {
}
