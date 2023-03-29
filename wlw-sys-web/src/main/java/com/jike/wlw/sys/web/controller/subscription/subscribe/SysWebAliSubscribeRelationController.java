package com.jike.wlw.sys.web.controller.subscription.subscribe;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationModifyRq;
import com.jike.wlw.sys.web.config.fegin.AliProductFeignClient;
import com.jike.wlw.sys.web.config.fegin.AliSubscribeRelationFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/3/6 10:17- hmc - 创建。
 */
@Api(value = "服务端订阅服务", tags = {"服务端订阅服务"})
@RestController
@RequestMapping(value = "/web/subscribeRelation", produces = "application/json;charset=utf-8")
public class SysWebAliSubscribeRelationController extends BaseController {

    @Autowired
    private AliSubscribeRelationFeignClient aliSubscribeRelationFeignClient;
    @Autowired
    private AliProductFeignClient aliProductFeignClient;

    @ApiOperation(value = "新建服务端订阅")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "添加服务端订阅请求参数") @RequestBody SubscribeRelationCreateRq createRq) throws BusinessException {
        try {
            String result = aliSubscribeRelationFeignClient.create(getTenantId(), createRq, "mengchen");
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "编辑服务端订阅")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "编辑服务端订阅请求参数") @RequestBody SubscribeRelationModifyRq modifyRq) throws BusinessException {
        try {
            aliSubscribeRelationFeignClient.modify(getTenantId(), modifyRq, "mengchen");
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除服务端订阅")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "删除服务端订阅请求参数") @RequestBody SubscribeRq subscribeRq) throws BusinessException {
        try {
            aliSubscribeRelationFeignClient.delete(getTenantId(), subscribeRq.getProductKey(), subscribeRq.getType(), subscribeRq.getIotInstanceId());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取服务端订阅")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<SubscribeRelation> get(@ApiParam(required = true, value = "删除服务端订阅请求参数") @RequestBody SubscribeRq subscribeRq) throws BusinessException {
        try {
            SubscribeRelation result = aliSubscribeRelationFeignClient.get(getTenantId(), subscribeRq.getProductKey(), subscribeRq.getType(), subscribeRq.getIotInstanceId());
            if (result != null) {
                Product product = aliProductFeignClient.get(getTenantId(), result.getProductKey(), subscribeRq.getIotInstanceId());
                result.setName(product.getName());
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增消费组订阅")
    @RequestMapping(value = "/addSubscribeRelation", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> addSubscribeRelation(@ApiParam(required = true, value = "添加消费组订阅请求参数") @RequestBody ConsumerGroupSubscribeCreateRq createRq) throws BusinessException {
        try {
            String result = aliSubscribeRelationFeignClient.addSubscribeRelation(getTenantId(), createRq, getUserName());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "移除指定消费组")
    @RequestMapping(value = "/deleteSubscribeRelation", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> deleteSubscribeRelation(@ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                                      @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                                                      @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {
            aliSubscribeRelationFeignClient.deleteSubscribeRelation(getTenantId(), groupId, productKey, getUserName(), iotInstanceId);
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
