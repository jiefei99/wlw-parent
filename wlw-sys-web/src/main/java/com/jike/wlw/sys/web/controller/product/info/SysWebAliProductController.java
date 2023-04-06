package com.jike.wlw.sys.web.controller.product.info;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.source.Source;
import com.jike.wlw.service.source.SourceTypes;
import com.jike.wlw.sys.web.config.fegin.AliProductFeignClient;
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

/**
 * 版权所有，极客软创（厦门）信息科技有限公司，2023，所有权利保留。
 * <p>
 * 修改历史：
 * 2023/3/6 10:17- hmc - 创建。
 */
@Api(value = "产品服务", tags = {"产品服务"})
@RestController
@RequestMapping(value = "/web/product", produces = "application/json;charset=utf-8")
public class SysWebAliProductController extends BaseController {

    @Autowired
    private AliProductFeignClient aliProductFeignClient;
    @Autowired
    private SysWebSourceController sourceController;

    @ApiOperation(value = "获取指定的产品信息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ActionResult<Product> get(@ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey, @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws Exception {
        try {
            Product result = new Product();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliProductFeignClient.get(getTenantId(), productKey, iotInstanceId);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "新增产品信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<String> create(@ApiParam(required = true, value = "新增产品请求参数") @RequestBody ProductCreateRq createRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");

            String result = null;
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliProductFeignClient.create(getTenantId(), createRq, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "修改产品信息")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> modify(@ApiParam(required = true, value = "修改产品请求参数") @RequestBody ProductModifyRq modifyRq) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliProductFeignClient.modify(getTenantId(), modifyRq, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }

            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "删除指定的产品信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> delete(
            @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws Exception {
        try {
            Assert.assertArgumentNotNull(productKey, "productKey");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliProductFeignClient.delete(getTenantId(), productKey, iotInstanceId, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "发布指定产品")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> publish(
            @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(productKey, "productKey");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliProductFeignClient.publishProduct(getTenantId(), productKey, iotInstanceId, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "取消指定产品的发布")
    @RequestMapping(value = "/unPublish", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<Void> unPublish(
            @ApiParam(required = true, value = "productKey") @RequestParam(value = "productKey") String productKey,
            @ApiParam(required = false, value = "实例Id") @RequestParam(value = "iotInstanceId") String iotInstanceId) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(productKey, "productKey");

            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                aliProductFeignClient.unPublishProduct(getTenantId(), productKey, iotInstanceId, AppContext.getContext().getUserName());
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok();
        } catch (Exception e) {
            return dealWithError(e);
        }
    }

    @ApiOperation(value = "根据查询条件查询产品")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public ActionResult<PagingResult<Product>> query(@ApiParam(required = true, value = "查询产品请求参数") @RequestBody ProductFilter filter) throws BusinessException {
        try {
            PagingResult<Product> result = new PagingResult<>();
            ActionResult<Source> source = sourceController.getConnectedSource();
            if (source == null || source.getData() == null) {
                return ActionResult.fail("未找到指定连接资源");
            }
            if (SourceTypes.ALIYUN.equals(source.getData().getType())) {
                result = aliProductFeignClient.query(getTenantId(), filter);
            } else {
                return ActionResult.fail("暂时只支持阿里云资源");
            }
            return ActionResult.ok(result);
        } catch (Exception e) {
            return dealWithError(e);
        }
    }
}
