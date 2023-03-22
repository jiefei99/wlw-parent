package com.jike.wlw.sys.web.controller.product.physicalModel;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCopyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPubTslGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishQueryRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelPublishRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelTsl;
import com.jike.wlw.service.physicalmodel.PhysicalModelTslGetRq;
import com.jike.wlw.sys.web.config.fegin.AliPhysicalModelFeignClient;
import com.jike.wlw.sys.web.controller.BaseController;
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

import java.util.List;
import java.util.Map;

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/3/6 10:17- hmc - 创建。
 */
@Api(value = "物模型服务", tags = {"物模型服务"})
@RestController
@RequestMapping(value = "/web/physicalModel", produces = "application/json;charset=utf-8")
public class SysWebAliPhysicalModelController extends BaseController {

    @Autowired
    private AliPhysicalModelFeignClient aliPhysicalModelFeignClient;

    @ApiOperation(value = "新增物模型信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> create(@ApiParam(required = true, value = "新增物模型请求参数") @RequestBody PhysicalModelCreateRq createRq) throws BusinessException {
        try {
            aliPhysicalModelFeignClient.create(getTenantId(), createRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取指定产品的物模型版本")
    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Void> getVersion(@ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
                                         @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {
            aliPhysicalModelFeignClient.getVersion(getTenantId(), productKey, iotInstanceId);
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "指定产品的已发布物模型TSL")
    @RequestMapping(value = "/getTslPublished", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PhysicalModelTsl> getTslPublished(@ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelPubTslGetRq modelGetRq) throws BusinessException {
        try {
            PhysicalModelTsl result = aliPhysicalModelFeignClient.getTslPublished(getTenantId(), modelGetRq);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定产品的物模型TSL")
    @RequestMapping(value = "/getTsl", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PhysicalModelTsl> getTsl(@ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelTslGetRq modelGetRq) throws BusinessException {
        try {
            PhysicalModelTsl result = aliPhysicalModelFeignClient.getTsl(getTenantId(), modelGetRq);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "指定品类的物模型信息")
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<PhysicalModel> getCategory(
            @ApiParam(required = false, value = "标识符") @RequestParam(value = "categoryKey") String categoryKey,
            @ApiParam(required = false, value = "资源组Id") @RequestParam(value = "resourceGroupId") String resourceGroupId,
            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {

            PhysicalModel result = aliPhysicalModelFeignClient.getCategory(getTenantId(), categoryKey, resourceGroupId, iotInstanceId);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "编辑物模型")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "编辑请求参数") @RequestBody PhysicalModelModifyRq modifyRq) throws BusinessException {
        try {
            aliPhysicalModelFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除指定的物模型信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(
            @ApiParam(required = true, value = "删除请求参数") @RequestBody PhysicalModelDelRq modelDelRq) throws Exception {
        try {
            aliPhysicalModelFeignClient.delete(getTenantId(), modelDelRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "复制物模型")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> copy(@ApiParam(required = true, value = "复制请求参数") @RequestBody PhysicalModelCopyRq modifyRq) throws BusinessException {
        try {
            aliPhysicalModelFeignClient.copy(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "发布指定物模型")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> publish(@ApiParam(required = true, value = "发布请求参数") @RequestBody PhysicalModelPublishRq publishRq) throws BusinessException {
        try {
            aliPhysicalModelFeignClient.publish(getTenantId(), publishRq, AppContext.getContext().getUserName());
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "获取物联网平台预定义的标准产品品类列表")
    @RequestMapping(value = "/queryTemplates", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<List<Map<String, String>>> queryTemplates(@ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {
            List<Map<String, String>> result = aliPhysicalModelFeignClient.queryTemplates(getTenantId(), iotInstanceId);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定产品已发布物模型中的功能定义详情")
    @RequestMapping(value = "/queryPublish", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PhysicalModel> queryPublish(@ApiParam(required = true, value = "查询物模型请求参数") @RequestBody PhysicalModelPublishQueryRq filter) throws BusinessException {
        try {
            PhysicalModel result = aliPhysicalModelFeignClient.queryPublish(getTenantId(), filter);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "查询指定产品物模型中的功能定义详情（包括未发布）")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PhysicalModel> get(@ApiParam(required = true, value = "查询请求参数") @RequestBody PhysicalModelGetRq modelGetRq) throws Exception {
        try {
            PhysicalModel result = aliPhysicalModelFeignClient.get(getTenantId(), modelGetRq);
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
