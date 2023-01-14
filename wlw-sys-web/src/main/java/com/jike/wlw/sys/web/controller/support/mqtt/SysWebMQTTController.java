package com.jike.wlw.sys.web.controller.support.mqtt;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.jike.wlw.service.support.iconconfig.IconConfig;
import com.jike.wlw.service.support.iconconfig.IconConfigEditRq;
import com.jike.wlw.service.support.iconconfig.IconConfigFilter;
import com.jike.wlw.sys.web.config.fegin.IconConfigFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "图标配置服务", tags = {"图标配置服务"})
@Slf4j
@RestController
@RequestMapping(value = "/web/mqtt", produces = "application/json;charset=utf-8")
public class SysWebMQTTController extends BaseController {

    @ApiOperation(value = "mqtt事件入口")
    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<IconConfig> event(@ApiParam(value = "参数") @RequestBody Map<String, Object> params) throws BusinessException {
        try {
            //连接成功
            System.out.println("事件触发: 参数" + JsonUtil.objectToJson(params));

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

}
