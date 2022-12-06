package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.author.org.member.OrgMemberService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author mengchen
 * @date 2022/7/21
 * @apiNote
 */
@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class)
public interface OrgMemberFeignClient extends OrgMemberService {
}
