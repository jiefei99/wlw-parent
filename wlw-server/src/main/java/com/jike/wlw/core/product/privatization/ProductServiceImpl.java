package com.jike.wlw.core.product.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.common.RandomString;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.InfluxDao;
import com.jike.wlw.dao.product.PProduct;
import com.jike.wlw.dao.product.ProductDao;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentFilter;
import com.jike.wlw.service.equipment.EquipmentService;
import com.jike.wlw.service.product.Product;
import com.jike.wlw.service.product.ProductCreateRq;
import com.jike.wlw.service.product.ProductFilter;
import com.jike.wlw.service.product.ProductModifyRq;
import com.jike.wlw.service.product.ProductQueryRq;
import com.jike.wlw.service.product.privatization.ProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanUtils;
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
@RestController("ProductServicePrivateImpl")
@ApiModel("私有产品服务实现")
public class ProductServiceImpl extends BaseService implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private InfluxDao influxDao;

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
            int nameSize = calcLength(createRq.getName());
            if (nameSize < 4 || nameSize > 30) {
                throw new BusinessException("产品名称长度为4~30个字符，请重新输入！");
            }
            if (createRq.getProtocolType() == null) {
                throw new BusinessException("设备接入网关的协议类型不能为空");
            }
            //校验 productName
            PProduct perz = productDao.get(PProduct.class, "name", createRq.getName(), "tenantId", tenantId);
            if (perz != null) {
                throw new BusinessException("指定的名称已用于其他产品，请重新输入！");
            }
            //生成 productKey
            String productKey = RandomString.getRandomString(10);
            perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId);
            while (true) {
                if (perz == null) {
                    break;
                }
                productKey = RandomString.getRandomString(10);
                perz = productDao.get(PProduct.class, "productKey", productKey, "tenantId", tenantId);
            }
            //生成 productSecret
            perz = new PProduct();
            String productSecret = RandomString.getRandomString(16);
            BeanUtils.copyProperties(createRq, perz);
            perz.setAuthType(createRq.getAuthType().getCaption());
            perz.onCreated(operator);
            perz.setProductId("rs");
            perz.setTenantId(tenantId);
            perz.setProductSecret(productSecret);
            perz.setProductKey(productKey);
            perz.setNetType(createRq.getNetType());
            productDao.save(perz);
            return perz.getProductId();
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
            PProduct perz = productDao.get(PProduct.class, "name", modifyRq.getName(), "tenantId", tenantId);
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
            EquipmentFilter filter = new EquipmentFilter();
            filter.setProductKeyEq(perz.getProductKey());
            List<Equipment> equipmentList = equipmentService.query(filter).getData();
            if (!CollectionUtils.isEmpty(equipmentList)) {
                throw new BusinessException("当前产品下存在未删除的设备，请删除设备后重试！");
            }
            productDao.remove(PProduct.class, perz.getUuid());
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
        PProduct perz = productDao.get(PProduct.class, "productKey", id, "tenantId", tenantId);
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

    //    计算长度，中文长度为2
    private int calcLength(String str) {
        int size = 0;
        int charNum;
        for (int i = 0; i < str.length(); i++) {
            charNum = str.charAt(i);
            if (19968 <= charNum && charNum < 40869) {
                size += 2;
            } else {
                size++;
            }
        }
        return size;
    }
}
