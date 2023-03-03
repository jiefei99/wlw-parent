/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/4/15 12:22 - chenpeisi - 创建。
 */
package com.jike.wlw.sys.web.config.fegin;


import com.jike.wlw.service.author.user.employee.EmployeeService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "wlw-server", decode404 = true, configuration = FeignConfiguration.class, path = "service/employee")
public interface EmployeeFeignClient extends EmployeeService {
}
