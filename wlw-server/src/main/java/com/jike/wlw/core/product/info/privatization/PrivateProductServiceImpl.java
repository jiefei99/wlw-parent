package com.jike.wlw.core.product.info.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.lang.Assert;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.info.ProductConverter;
import com.jike.wlw.dao.InfluxDao;
import com.jike.wlw.dao.equipment.EquipmentDao;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.dao.product.info.ProductDao;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentFilter;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.privatization.PrivateEquipmentService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelManagerService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModuleCreateRq;
import com.jike.wlw.service.product.info.AuthType;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.PublishStateType;
import com.jike.wlw.service.product.info.privatization.PrivateProductService;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.privatization.PrivateSubscribeRelationService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title: ProductServiceImpl
 * @Author RS
 * @Date: 2023/1/11 18:10
 * @Version 1.0
 */
@Slf4j
@RestController("productServicePrivateImpl")
@ApiModel("私有产品服务实现")
public class PrivateProductServiceImpl extends BaseService implements PrivateProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private PrivateEquipmentService equipmentService;
    @Autowired
    private PhysicalModelManagerService modelManagerService;
    @Autowired
    private InfluxDao influxDao;
    @Autowired
    private PrivateSubscribeRelationService subscribeRelationService;

    @Override
    public Product get(String tenantId, String productKey, String iotInstanceId) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PProduct perz = doGet(tenantId, productKey);
            if (perz == null) {
                return null;
            }
            Product result = ProductConverter.coverProduct(perz);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String create(String tenantId, ProductCreateRq createRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(createRq, "createRq");
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("产品名称不能为空");
            }
            int nameSize = StringRelevant.calcStrLength(createRq.getName());
            if (nameSize < 4 || nameSize > 30) {
                throw new BusinessException("产品名称长度为4~30个字符，请重新输入！");
            }
            if (createRq.getProtocolType() == null) {
                throw new BusinessException("设备接入网关的协议类型不能为空");
            }
            //校验 productName
            PProduct perz = productDao.get(PProduct.class, "name", createRq.getName(), "tenantId", tenantId, "isDeleted", 0);
            if (perz != null) {
                throw new BusinessException("指定的名称已用于其他产品，请重新输入！");
            }
            //生成 productKey
            String productKey = StringRelevant.buildRandomString(10);
            perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId, "isDeleted", 0);
            while (true) {
                if (perz == null) {
                    break;
                }
                productKey = StringRelevant.buildRandomString(10);
                perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId, "isDeleted", 0);
            }
            perz = ProductConverter.ProductCover(createRq, tenantId, productKey, operator);
            productDao.save(perz);
            PhysicalModelModuleCreateRq moduleCreateRq = new PhysicalModelModuleCreateRq();
            moduleCreateRq.setProductKey(perz.getProductKey());
            moduleCreateRq.setName("默认模块");
            moduleCreateRq.setIdentifier("default");
            modelManagerService.createModule(tenantId, moduleCreateRq, operator);
            return perz.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, ProductModifyRq modifyRq, String operator) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(modifyRq, "modifyRq");
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getProductKey())) {
                throw new BusinessException("产品ProductKey不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getName())) {
                throw new BusinessException("产品名称不能为空");
            }
            if (StringUtils.isNotBlank(modifyRq.getDescription()) && modifyRq.getDescription().length() > 100) {
                throw new BusinessException("产品描述信息长度不能超过100个字符，请重新输入！");
            }
            PProduct perz = productDao.get(PProduct.class, "name", modifyRq.getName(), "tenantId", tenantId, "isDeleted", 0);
            if (perz != null) {
                throw new BusinessException("指定的名称已用于其他产品，请重新输入！");
            }
            perz = doGet(tenantId, modifyRq.getProductKey());
            if (perz == null) {
                throw new BusinessException("指定产品不存在或已删除，请确认产品密钥是否正确");
            }
            if (perz.getProductStatus().equals(PublishStateType.RELEASE_STATUS)) {
                throw new BusinessException("产品已发布，无法修改");
            }
            perz.setName(modifyRq.getName());
            if (!StringUtil.isNullOrBlank(modifyRq.getDescription())) {
                perz.setDescription(modifyRq.getDescription());
            }
            if (!StringUtil.isNullOrBlank(modifyRq.getRemark())) {
                perz.setRemark(modifyRq.getRemark());
            }
            perz.onModified(operator);
            productDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String tenantId, String productKey, String iotInstanceId, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("需要删除的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PProduct perz = doGet(tenantId, productKey);
            if (perz == null) {
                throw new BusinessException("不存在此产品");
            }
            if (perz.getProductStatus().equals(PublishStateType.RELEASE_STATUS)) {
                throw new BusinessException("产品已发布，无法删除");
            }
            EquipmentQueryByProductRq filter = new EquipmentQueryByProductRq();
            filter.setProductKey(perz.getProductKey());
            List<Equipment> equipmentList = equipmentService.queryByProductKey(tenantId, filter).getData();
            if (!CollectionUtils.isEmpty(equipmentList)) {
                throw new BusinessException("当前产品下存在未删除的设备，请删除设备后重试！");
            }
            perz.setIsDeleted(1);
            perz.onModified(operator);
            productDao.save(perz);
            subscribeRelationService.delete(tenantId, productKey, SubscribeRelation.AMQP, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Product> query(String tenantId, ProductFilter filter) throws BusinessException {
        try {
            Assert.assertArgumentNotNull(filter, "filter");
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            if (filter.getPage() < 1) {
                throw new BusinessException("页数默认从第一页开始显示");
            }
            if (filter.getPageSize() < 1 || filter.getPageSize() > 200) {
                throw new BusinessException("每页显示的产品数量不能超过200个");
            }
            filter.setTenantId(tenantId);
            List<PProduct> list = productDao.query(filter);
            long count = productDao.getCount(filter);
            List<Product> result = new ArrayList<>();
            for (PProduct perz : list) {
                Product product = new Product();
                product.setAuthType(AuthType.map.get(perz.getAuthType()));
                product.setDataFormat(perz.getDataFormat());
                product.setDescription(perz.getDescription());
                product.setCreated(perz.getCreated());
                product.setNodeType(perz.getNodeType());
                product.setProductKey(perz.getProductKey());
                product.setName(perz.getName());

                EquipmentFilter equipmentFilter = new EquipmentFilter();
                equipmentFilter.setTenantIdEq(tenantId);
                equipmentFilter.setProductKeyEq(perz.getProductKey());
                product.setDeviceCount((int) equipmentDao.getCount(equipmentFilter));
                if (!StringUtil.isNullOrBlank(perz.getPhysicalModelIdsJson())) {
                    product.setPhysicalModelIds(JsonUtil.jsonToArrayList(perz.getPhysicalModelIdsJson(), String.class));
                }
                result.add(product);
            }
            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void publishProduct(String tenantId, String productKey, String iotInstanceId, String operator) {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("需要发布的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PProduct perz = doGet(tenantId, productKey);
            if (perz == null) {
                throw new BusinessException("不存在此产品");
            }
            if (perz.getProductSecret().equals(PublishStateType.RELEASE_STATUS)) {
                throw new BusinessException("产品已发布！");
            }
            perz.setProductStatus(PublishStateType.RELEASE_STATUS.toString());
            perz.onModified(operator);
            productDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void unPublishProduct(String tenantId, String productKey, String iotInstanceId, String operator) {
        try {
            if (StringUtils.isBlank(productKey)) {
                throw new BusinessException("取消发布的产品的ProductKey不能为空");
            }
            if (StringUtils.isBlank(tenantId)) {
                throw new BusinessException("租户不能为空");
            }
            PProduct perz = doGet(tenantId, productKey);
            if (perz == null) {
                throw new BusinessException("不存在此产品");
            }
            if (perz.getProductStatus().equals(PublishStateType.DEVELOPMENT_STATUS)) {
                throw new BusinessException("产品未发布，不能取消发布！");
            }
            perz.setProductStatus(PublishStateType.DEVELOPMENT_STATUS.toString());
            perz.onModified(operator);
            productDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private PProduct doGet(String tenantId, String id) throws Exception {
        PProduct perz = productDao.get(PProduct.class, "productKey", id, "tenantId", tenantId, "isDeleted", 0);
        if (perz == null) {
            perz = productDao.get(PProduct.class, id);
        }
        return perz;
    }

    @Override
    public void saveInflux(String measurement, Map<String, Object> fields, Map<String, String> tags) throws BusinessException {
        influxDao.saveMessage(measurement, fields, tags);
    }

    @Override
    public Object queryInflux(String measurement, String command) throws BusinessException {
        QueryResult message = influxDao.getMessage(measurement, command);
        return message;
    }

}
