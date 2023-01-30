package com.jike.wlw.service.message;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZhengZhouDong
 * @Date 2020/5/14 17:29
 */
@Api(tags = "消息服务")
@RequestMapping(value = "/service/message", produces = "application/json;charset=utf-8")
public interface MessageService {

    @ApiOperation(value = "根据uuid获取消息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Message get(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                @ApiParam(required = true, value = "消息uuid") @RequestParam("uuid") String id) throws BusinessException;

    @ApiOperation(value = "根据uuid设置消息已读")
    @RequestMapping(value = "/setRead", method = RequestMethod.POST)
    @ResponseBody
    void setRead(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                 @ApiParam(required = true, value = "uuid") @RequestBody String uuid,
                 @ApiParam(required = true, value = "操作人") @RequestParam("operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据用户ID统计未读消息数量")
    @RequestMapping(value = "/countUnRead", method = RequestMethod.POST)
    @ResponseBody
    unReadNum countUnRead(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                          @ApiParam(value = "用户id") @RequestBody String userId) throws BusinessException;

    @ApiOperation(value = "新增消息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    String create(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                  @ApiParam(required = true, value = "message") @RequestBody Message message,
                  @ApiParam(required = true, value = "操作人") @RequestParam("operator") String operator) throws BusinessException;

    @ApiOperation(value = "根据查询条件查询消息")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    PagingResult<Message> query(@ApiParam(required = true, value = "租户ID") @RequestParam(value = "tenantId") String tenantId,
                                @ApiParam(required = true, value = "查询条件") @RequestBody MessageFilter filter) throws BusinessException;

}
