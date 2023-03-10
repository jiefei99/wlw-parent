package com.jike.wlw.sys.web.controller.serverSubscription.consumptionGroup;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupDeleteRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupVO;
import com.jike.wlw.service.serverSubscription.consumerGroup.vo.ConsumerGroupStatusVO;
import com.jike.wlw.sys.web.config.fegin.AliConsumerGroupFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: ConsumptionGroupController
 * @Author RS
 * @Date: 2023/3/6 14:41
 * @Version 1.0
 */
@Slf4j
@Api(value = "消费组", tags = {"消息订阅"})
@RestController
@RequestMapping(value = "/web/consumptionGroup", produces = "application/json;charset=utf-8")
public class SysWebConsumptionGroupController extends BaseController {
    @Autowired
    private AliConsumerGroupFeignClient consumerGroupFeignClient;

    @ApiOperation(value = "获取指定的消费组信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<ConsumerGroup> get(@ApiParam(required = true, value = "groupId") @RequestParam(value = "groupId") String groupId,
                                           @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        try {
            ConsumerGroup consumerGroup = consumerGroupFeignClient.get(getTenantId(), groupId, iotInstanceId);
            return ActionResult.ok(consumerGroup);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取指定的消费组状态")
    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<ConsumerGroupStatusVO> getStatus(@RequestParam(value = "groupId") String groupId,
                                            @ApiParam(required = false, value = "iotInstanceId") @RequestParam(value = "iotInstanceId",required = false) String iotInstanceId) throws Exception {
        try {
            ConsumerGroupStatusVO status = consumerGroupFeignClient.getStatus(getTenantId(), groupId, iotInstanceId);
            return ActionResult.ok(status);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询消费组")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<ConsumerGroupVO>> query(@RequestBody ConsumerGroupFilter filter) throws BusinessException {
        try {
            if (filter != null && StringUtils.isNotBlank(filter.getNameLike())) {
                filter.setFuzzy(true);
            }
            PagingResult<ConsumerGroupVO> query = consumerGroupFeignClient.query(getTenantId(), filter);
            return ActionResult.ok(query);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除消费组")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(@RequestBody ConsumerGroupDeleteRq deleteRq) throws BusinessException {
        try {
            if (deleteRq == null || StringUtils.isBlank(deleteRq.getId())) {
                throw new BusinessException("删除消费组ID不能为空");
            }
            consumerGroupFeignClient.delete(getTenantId(), deleteRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新建消费组")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@RequestBody ConsumerGroupCreateRq createRq) throws BusinessException {
        try {
            if (createRq == null) {
                throw new BusinessException("新建条件不能为空");
            }
            String id = consumerGroupFeignClient.create(getTenantId(), createRq, getUserName());
            return ActionResult.ok(id);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改消费组")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@RequestBody ConsumerGroupModifyRq modifyRq) throws BusinessException {
        try {
            if (modifyRq == null) {
                throw new BusinessException("修改条件不能为空");
            }
            consumerGroupFeignClient.modify(getTenantId(), modifyRq, getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}


