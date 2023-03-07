package com.jike.wlw.service.serverSubscription.consumerGroup.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.serverSubscription.consumerGroup.BaseConsumerGroupService;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupDeleteRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(tags = "私有化消费组")
@RequestMapping(value = "service/consumerGroupPrivate", produces = "application/json;charset=utf-8")
public interface PrivateConsumerGroupService extends BaseConsumerGroupService {

    @ApiOperation(value = "新建消费组")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "添加消费组请求参数") @RequestBody ConsumerGroupCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑消费组")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑消费组请求参数") @RequestBody ConsumerGroupModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除产品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "消费组") @RequestBody ConsumerGroupDeleteRq deleteRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "清空消费组堆积消息")
    @RequestMapping(value = "/resetConsumerGroupPosition", method = RequestMethod.POST)
    @ResponseBody
    void resetPosition(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                       @ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                       @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "根据ID获取消费组")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    ConsumerGroup get(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                      @ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                      @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

    @ApiOperation(value = "获取消费组列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<ConsumerGroupVO> query(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                        @ApiParam(required = true, value = "查询消费组请求参数") @RequestBody ConsumerGroupFilter filter) throws BusinessException;

    @ApiOperation(value = "获取消费组状态")
    @RequestMapping(value = "/queryConsumerGroupStatus", method = RequestMethod.GET)
    @ResponseBody
    List<ConsumerGroup> getStatus(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                                  @ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                  @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException;

}
