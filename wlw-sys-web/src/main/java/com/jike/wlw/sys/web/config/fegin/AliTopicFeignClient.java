package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.product.topic.ali.AliTopicService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/aliTopic")
public interface AliTopicFeignClient extends AliTopicService {
}
