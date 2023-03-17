package com.jike.wlw.service.config;

import com.geeker123.rumba.commons.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

@Api(tags = "配置服务")
public interface ConfigService {

    @ApiOperation("获取指定的配置项")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    Config get(@ApiParam(required = true, value = "配置组名称") @RequestParam(value = "group") String group,
                                                   @ApiParam(required = true, value = "配置组下级配置项KEY") @RequestParam(value = "key") String key) throws BusinessException;

    @ApiOperation("获取指定的配置组")
    @RequestMapping(value = "/getGroup", method = RequestMethod.GET)
    @ResponseBody
    ConfigGroup getGroup(@ApiParam(required = true, value = "配置组名称") @RequestParam(value = "group") String group) throws BusinessException;

    @ApiOperation("新增/修改配置项")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    String save(@ApiParam(required = true, value = "配置项") @RequestBody Config config,
                @ApiParam(required = false, value = "操作人") @RequestParam(value = "operator", required = false) String operator) throws BusinessException;

    @ApiOperation("新增/修改配置组")
    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    @ResponseBody
    void saveGroup(@ApiParam(required = true, value = "配置组") @RequestBody ConfigGroup group,
                   @ApiParam(required = true, value = "操作人") @RequestParam(value = "operator") String operator) throws BusinessException;

    @ApiOperation("删除配置组")
    @RequestMapping(value = "/deleteGroup", method = RequestMethod.POST)
    @ResponseBody
    void deleteGroup(@ApiParam(required = true, value = "配置组名称") @RequestParam(value = "group") String group) throws BusinessException;
}
