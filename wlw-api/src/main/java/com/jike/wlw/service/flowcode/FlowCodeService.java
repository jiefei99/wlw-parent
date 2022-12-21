/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/7/25 15:46 - chenpeisi - 创建。
 */
package com.jike.wlw.service.flowcode;

import com.geeker123.rumba.commons.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 流水号服务
 */
@Api(tags = "流水号服务")
public interface FlowCodeService {

    @ApiOperation(value = "下个流水")
    @RequestMapping(value = "/next", method = RequestMethod.GET)
    @ResponseBody
    String next(@ApiParam(required = true, value = "流水号名称") @RequestParam(value = "flowName") String flowName,
                @ApiParam(required = true, value = "前缀") @RequestParam(value = "prefix") String prefix,
                @ApiParam(required = true, value = "流水号长度") @RequestParam(value = "flowLength") int flowLength) throws BusinessException;

    @ApiOperation(value = "下个流水号不带时间前缀")
    @RequestMapping(value = "/nextWithoutTime", method = RequestMethod.GET)
    @ResponseBody
    String nextWithoutTime(@ApiParam(required = true, value = "流水号名称") @RequestParam(value = "flowName") String flowName,
                           @ApiParam(required = true, value = "前缀") @RequestParam(value = "prefix") String prefix,
                           @ApiParam(required = true, value = "流水号长度") @RequestParam(value = "flowLength") int flowLength) throws BusinessException;
}