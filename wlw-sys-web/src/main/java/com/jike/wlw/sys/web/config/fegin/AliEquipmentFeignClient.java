package com.jike.wlw.sys.web.config.fegin;

import com.jike.wlw.service.equipment.ali.AliEquipmentService;
import com.jike.wlw.service.operation.log.OperationLogService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/2/23 17:22- zhengzhoudong - 创建。
 */
@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/equipment/aliyun")
public interface AliEquipmentFeignClient extends AliEquipmentService {
}
