package com.jike.wlw.sys.web.controller.product.topic;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.jike.wlw.service.product.topic.Topic;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceTypes;
import com.jike.wlw.sys.web.config.fegin.AliTopicFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import com.jike.wlw.sys.web.controller.source.SysWebSourceController;
import com.jike.wlw.sys.web.sso.AppContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "Topic服务", tags = {"Topic服务"})
@RestController
@RequestMapping(value = "/web/topic", produces = "application/json;charset=utf-8")
public class SysWebTopicController extends BaseController {

    @Autowired
    private AliTopicFeignClient aliTopicFeignClient;
    @Autowired
    private SysWebSourceController sourceController;

    @ApiOperation(value = "新增Topic信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增Topic请求参数") @RequestBody TopicCreateRq createRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");

            String result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliTopicFeignClient.create(getTenantId(), createRq, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改Topic信息")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改Topic请求参数") @RequestBody TopicModifyRq modifyRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliTopicFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除指定的Topic信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(
            @ApiParam(required = true, value = "topicId") @RequestParam(value = "topicId") String topicId,
            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws Exception {
        try {
            Assert.assertArgumentNotNull(topicId, "topicId");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliTopicFeignClient.delete(getTenantId(), topicId, iotInstanceId, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询Topic")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<List<Topic>> query(@ApiParam(required = true, value = "查询Topic请求参数") @RequestBody TopicFilter filter) throws BusinessException {
        try {
            List<Topic> result = new ArrayList<>();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliTopicFeignClient.query(getTenantId(), filter);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
