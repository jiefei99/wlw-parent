package com.jike.wlw.service.product.topic.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProductQueryRq;
import com.jike.wlw.service.product.topic.BaseTopicService;
import com.jike.wlw.service.product.topic.Topic;
import com.jike.wlw.service.product.topic.TopicCreateRq;
import com.jike.wlw.service.product.topic.TopicFilter;
import com.jike.wlw.service.product.topic.TopicModifyRq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(tags = "私有化Topic服务")
@RequestMapping(value = "service/topicPrivate", produces = "application/json;charset=utf-8")
public interface PrivateTopicService extends BaseTopicService {

    @ApiOperation(value = "根据ID获取Topic")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    List<Topic> query(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                      @ApiParam(required = true, value = "查询Topic请求参数") @RequestBody TopicFilter filter) throws BusinessException;

    @ApiOperation(value = "新建Topic")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "添加请求参数") @RequestBody TopicCreateRq createRq,
                  @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "编辑Topic")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    void modify(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "编辑请求参数") @RequestBody TopicModifyRq modifyRq,
                @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据ID删除Topic")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    void delete(@ApiParam(required = true, value = "租户") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "topicId") @RequestParam(value = "topicId")String topicId,
                @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId")String iotInstanceId) throws BusinessException;

}
