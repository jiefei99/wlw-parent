package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.author.login.LoginService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class)
public interface LoginFeignClient extends LoginService {
}
