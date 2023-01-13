/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/7/25 16:04 - chenpeisi - 创建。
 */
package com.jike.wlw.config.fegin;


import com.jike.wlw.service.flowcode.FlowCodeService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 流水号服务
 */
@FeignClient(name = "bsc-server", decode404 = true, configuration = FeignConfiguration.class)
public interface FlowCodeFeignClient extends FlowCodeService {
}
