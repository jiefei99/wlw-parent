package com.jike.wlw.sys.web.controller.product.info;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.sys.web.config.fegin.AliConsumerGroupFeignClient;
import com.jike.wlw.sys.web.config.fegin.AliProductFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/3/6 10:17- hmc - 创建。
 */
@Api(value = "产品服务", tags = {"产品服务"})
@RestController
@RequestMapping(value = "/web/product", produces = "application/json;charset=utf-8")
public class SysWebAliProductController extends BaseController {

    @Autowired
    private AliProductFeignClient aliProductFeignClient;

    @ApiOperation(value = "根据查询条件查询产品")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Product>> query(@ApiParam(required = true, value = "查询产品请求参数") @RequestBody ProductFilter filter) throws BusinessException{
        try {
            PagingResult<Product> result = aliProductFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
