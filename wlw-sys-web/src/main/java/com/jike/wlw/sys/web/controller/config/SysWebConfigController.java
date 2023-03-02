package com.jike.wlw.sys.web.controller.config;

import com.jike.wlw.sys.web.config.fegin.ConfigFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "配置服务", tags = {"配置服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/config", produces = "application/json;charset=utf-8")
public class SysWebConfigController extends BaseController {

//    @Autowired
//    private ConfigFeignClient configFeignClient;

//    @ApiOperation(value = "新增/修改配置项")
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    @ResponseBody
//    public ActionResult<String> save(@ApiParam(required = true, value = "配置项") @RequestBody Config config) {
//        try {
//            String result = configFeignClient.save(config, getUserName());
//
//            return ActionResult.ok(result);
//        } catch (Exception e) {
//            return dealWithError(e);
//        }
//    }
//
//    @ApiOperation("获取指定的配置项")
//    @RequestMapping(value = "/get", method = RequestMethod.POST)
//    @ResponseBody
//    public ActionResult<Config> get(@ApiParam(required = true, value = "配置组名称") @RequestBody Config config) throws BusinessException {
//        try {
//            Config result = configFeignClient.get(config.getConfigGroup(), config.getConfigKey());
//
//            return ActionResult.ok(result);
//        } catch (Exception e) {
//            return dealWithError(e);
//        }
//    }
//
//    @ApiOperation("获取指定的配置组")
//    @RequestMapping(value = "/getGroup", method = RequestMethod.POST)
//    @ResponseBody
//    ActionResult<ConfigGroup> getGroup(@ApiParam(required = true, value = "配置组名称") @RequestBody Config config) throws BusinessException {
//        try {
//            ConfigGroup result = configFeignClient.getGroup(config.getConfigGroup());
//
//            return ActionResult.ok(result);
//        } catch (Exception e) {
//            return dealWithError(e);
//        }
//    }


}
