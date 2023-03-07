package com.jike.wlw.service.serverSubscription.subscribe.ali;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupSubscribeCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationModifyRq;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelationCreateRq;
import com.jike.wlw.service.serverSubscription.subscribe.BaseSubscribeRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "阿里服务端订阅")

public interface AliSubscribeRelationService extends BaseSubscribeRelationService {
    @ApiOperation(value = "新建服务端订阅")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "添加服务端订阅请求参数") @RequestBody SubscribeRelationCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑服务端订阅")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑服务端订阅请求参数") @RequestBody SubscribeRelationModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "删除服务端订阅")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                @ApiParam(required = true, value = "订阅类型") @RequestParam(value = "type") String type,
                @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "获取服务端订阅")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    SubscribeRelation get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                          @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                          @ApiParam(required = true, value = "订阅类型") @RequestParam(value = "type") String type,
                          @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

        @ApiOperation(value = "新增消费组订阅")
    @RequestMapping(value = "/addSubscribeRelation", method = RequestMethod.POST)
    @ResponseBody
    String addSubscribeRelation(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                   @ApiParam(required = true, value = "添加消费组订阅请求参数") @RequestBody ConsumerGroupSubscribeCreateRq createRq,
                                   @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;


        @ApiOperation(value = "移除指定消费组")
    @RequestMapping(value = "/deleteSubscribeRelation", method = RequestMethod.POST)
    @ResponseBody
    void deleteSubscribeRelation(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                 @ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                 @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                                 @ApiParam(required = false, value = "operator") @RequestParam(value = "operator") String operator,
                                 @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

}
