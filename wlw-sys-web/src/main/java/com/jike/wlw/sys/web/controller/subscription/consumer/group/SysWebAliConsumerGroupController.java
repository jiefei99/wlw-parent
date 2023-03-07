package com.jike.wlw.sys.web.controller.subscription.consumer.group;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.serverSubscription.consumerGroup.*;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupVO;
import com.jike.wlw.sys.web.config.fegin.AliConsumerGroupFeignClient;
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
@Api(value = "消费组服务", tags = {"消费组服务"})
@RestController
@RequestMapping(value = "/web/consumerGroup", produces = "application/json;charset=utf-8")
public class SysWebAliConsumerGroupController extends BaseController {

    @Autowired
    private AliConsumerGroupFeignClient aliConsumerGroupFeignClient;

    @ApiOperation(value = "新建消费组")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "添加消费组请求参数") @RequestBody ConsumerGroupCreateRq createRq) throws BusinessException{
        try {
            String result = aliConsumerGroupFeignClient.create(getTenantId(), createRq, getUserName());
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "编辑消费组")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "编辑消费组请求参数") @RequestBody ConsumerGroupModifyRq modifyRq) throws BusinessException{
        try {
            aliConsumerGroupFeignClient.modify(getTenantId(), modifyRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据ID删除产品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@ApiParam(required = true, value = "消费组Id") @RequestBody ConsumerGroupDeleteRq consumerGroupDeleteRq,
                                     @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId)throws BusinessException{
        try {
            aliConsumerGroupFeignClient.delete(getTenantId(), consumerGroupDeleteRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "清空消费组堆积消息")
    @RequestMapping(value = "/resetConsumerGroupPosition", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> resetPosition(@ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException{
        try {
            aliConsumerGroupFeignClient.resetPosition(getTenantId(), groupId, iotInstanceId);
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }


    @ApiOperation(value = "根据ID获取消费组")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<ConsumerGroup> get(@ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                           @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException{
        try {
            ConsumerGroup result = aliConsumerGroupFeignClient.get(getTenantId(), groupId, iotInstanceId);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取消费组列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<ConsumerGroup>> query(@ApiParam(required = true, value = "查询消费组请求参数") @RequestBody ConsumerGroupFilter filter) throws BusinessException{
        try {
            PagingResult<ConsumerGroupVO> result = aliConsumerGroupFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取消费组状态")
    @RequestMapping(value = "/queryConsumerGroupStatus", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<List<ConsumerGroup>> getStatus(@ApiParam(required = true, value = "消费组Id") @RequestParam(value = "groupId") String groupId,
                                                       @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException{
        try {
            List<ConsumerGroup> result = aliConsumerGroupFeignClient.getStatus(getTenantId(), groupId, iotInstanceId);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
