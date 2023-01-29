package com.jike.wlw.core.product.info.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.InfluxDao;
import com.jike.wlw.dao.product.info.PProduct;
import com.jike.wlw.dao.product.info.ProductDao;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.ali.AliEquipmentService;
import com.jike.wlw.service.product.info.Product;
import com.jike.wlw.service.product.info.ProductCreateRq;
import com.jike.wlw.service.product.info.ProductFilter;
import com.jike.wlw.service.product.info.ProductModifyRq;
import com.jike.wlw.service.product.info.ProductQueryRq;
import com.jike.wlw.service.product.info.privatization.PrivateProductService;
import com.jike.wlw.service.serverSubscription.subscribe.SubscribeRelation;
import com.jike.wlw.service.serverSubscription.subscribe.privatization.PrivateSubscribeRelationService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
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
    private AliEquipmentService aliEquipmentService;
    @Autowired
    private InfluxDao influxDao;
    @Autowired
    private PrivateSubscribeRelationService subscribeRelationService;

    @Override
    public Product get(String tenantId, ProductQueryRq productQueryRq) throws BusinessException {
        try {
            PProduct perz = doGet(tenantId, productQueryRq.getProductKey());
            if (perz == null) {
                return null;
            }
            Product result = new Product();
            BeanUtils.copyProperties(perz, result);
            if (!StringUtil.isNullOrBlank(perz.getPhysicalModelIdsJson())) {
                result.setPhysicalModelIds(JsonUtil.jsonToArrayList(perz.getPhysicalModelIdsJson(), String.class));
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public String create(String tenantId, ProductCreateRq createRq, String operator) throws BusinessException {
        try {
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
            PProduct perz = productDao.get(PProduct.class, "name", createRq.getName(), "tenantId", tenantId,"isDeleted",0);
            if (perz != null) {
                throw new BusinessException("指定的名称已用于其他产品，请重新输入！");
            }
            //生成 productKey
            String productKey = StringRelevant.buildRandomString(10);
            perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId,"isDeleted",0);
            while (true) {
                if (perz == null) {
                    break;
                }
                productKey = StringRelevant.buildRandomString(10);
                perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId,"isDeleted",0);
            }
            //生成 productSecret
            perz = new PProduct();
            String productSecret = StringRelevant.buildRandomString(16);
            BeanUtils.copyProperties(createRq, perz);
            perz.setAuthType(createRq.getAuthType().getCaption());
            perz.onCreated(operator);
            perz.setId(StringRelevant.buildId(20));
            perz.setTenantId(tenantId);
            perz.setProductSecret(productSecret);
            perz.setProductKey(productKey);
            perz.setNetType(createRq.getNetType());
            perz.onCreated(operator);
            productDao.save(perz);
            return perz.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void modify(String tenantId, ProductModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (StringUtils.isBlank(modifyRq.getProductKey())) {
                throw new BusinessException("产品ProductKey不能为空");
            }
            if (StringUtils.isBlank(modifyRq.getName())) {
                throw new BusinessException("产品名称不能为空");
            }
            if (StringUtils.isNotBlank(modifyRq.getDescription()) && modifyRq.getDescription().length() > 100) {
                throw new BusinessException("产品描述信息长度不能超过100个字符，请重新输入！");
            }
            PProduct perz = productDao.get(PProduct.class, "name", modifyRq.getName(), "tenantId", tenantId,"isDeleted",0);
            if (perz != null) {
                throw new BusinessException("指定的名称已用于其他产品，请重新输入！");
            }
            perz = doGet(tenantId, modifyRq.getProductKey());
            if (perz == null) {
                throw new BusinessException("指定产品不存在或已删除，请确认产品密钥是否正确");
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
    public void delete(String tenantId, String productKey, String iotInstanceId) throws BusinessException {
        try {
            PProduct perz = doGet(tenantId, productKey);
            if (perz == null) {
                throw new BusinessException("不存在此产品");
            }
            EquipmentQueryByProductRq filter = new EquipmentQueryByProductRq();
            filter.setProductKey(perz.getProductKey());
            //todo 得改成私有产品
            List<Equipment> equipmentList = aliEquipmentService.queryByProductKey(tenantId, filter).getData();
            if (!CollectionUtils.isEmpty(equipmentList)) {
                throw new BusinessException("当前产品下存在未删除的设备，请删除设备后重试！");
            }
            perz.setIsDeleted(1);
            productDao.save(perz);
            subscribeRelationService.delete(tenantId,productKey, SubscribeRelation.AMQP,null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Product> query(String tenantId, ProductQueryRq productQueryRq) throws BusinessException {
        try {
            if (productQueryRq.getCurrentPage() < 1) {
                throw new BusinessException("页数默认从第一页开始显示");
            }
            if (productQueryRq.getPageSize() < 1 || productQueryRq.getPageSize() > 200) {
                throw new BusinessException("每页显示的产品数量不能超过200个");
            }
            ProductFilter filter = new ProductFilter();
            filter.setIdIn(productQueryRq.getIdIn());
            filter.setProductKeyEq(productQueryRq.getProductKey());
            filter.setNameEq(productQueryRq.getNameEq());
            filter.setTenantId(tenantId);
            List<PProduct> list = productDao.query(filter);
            long count = productDao.getCount(filter);
            List<Product> result = new ArrayList<>();
            for (PProduct perz : list) {
                Product product = new Product();
                BeanUtils.copyProperties(perz, product);
                if (!StringUtil.isNullOrBlank(perz.getPhysicalModelIdsJson())) {
                    product.setPhysicalModelIds(JsonUtil.jsonToArrayList(perz.getPhysicalModelIdsJson(), String.class));
                }
                result.add(product);
            }
            return new PagingResult<>(productQueryRq.getCurrentPage(), productQueryRq.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private PProduct doGet(String tenantId, String id) throws Exception {
        PProduct perz = productDao.get(PProduct.class, "productKey", id, "tenantId", tenantId,"isDeleted",0);
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
