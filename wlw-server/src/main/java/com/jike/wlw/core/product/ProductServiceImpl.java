package com.jike.wlw.core.product;

import com.aliyun.iot20180120.models.CreateProductResponseBody;
import com.aliyun.iot20180120.models.DeleteProductResponse;
import com.aliyun.iot20180120.models.UpdateProductResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.product.iot.IemProductManager;
import com.jike.wlw.core.product.iot.RegisterProductRq;
import com.jike.wlw.dao.InfluxDao;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.product.PProduct;
import com.jike.wlw.dao.product.ProductDao;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentFilter;
import com.jike.wlw.service.equipment.EquipmentService;
import com.jike.wlw.service.product.Product;
import com.jike.wlw.service.product.ProductCreateRq;
import com.jike.wlw.service.product.ProductFilter;
import com.jike.wlw.service.product.ProductModifyRq;
import com.jike.wlw.service.product.ProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@ApiModel("产品服务实现")
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private IemProductManager productManager;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private InfluxDao influxDao;

    @Override
    public Product get(String id) throws BusinessException {
        try {
            PProduct perz = doGet(id);
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

    @TX
    @Override
    public void create(ProductCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("产品名称不能为空");
            }
            if (createRq.getNodeType() == null) {
                throw new BusinessException("节点类型不能为空");
            }
            if (createRq.getDataFormat() == null) {
                throw new BusinessException("数据格式不能为空");
            }
            if (createRq.getProtocolType() == null) {
                throw new BusinessException("设备接入网关的协议类型不能为空");
            }

            RegisterProductRq registerRq = new RegisterProductRq();
            registerRq.setNodeType(createRq.getNodeType());
            registerRq.setProductName(createRq.getName());
            registerRq.setDataFormat(createRq.getDataFormat());
            registerRq.setProtocolType(createRq.getProtocolType());
            registerRq.setDescription(createRq.getDescription());

            CreateProductResponseBody response = productManager.registerProduct(registerRq);

            if (response.getSuccess()) {
                PProduct perz = new PProduct();
                BeanUtils.copyProperties(createRq, perz);
                perz.setProductKey(response.getProductKey());
                perz.setProductSecret(response.getData().getProductSecret());

                perz.onCreated(operator);

                productDao.save(perz);
            } else {
                throw new BusinessException("新建产品失败，原因：" + response.getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(ProductModifyRq modifyRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(modifyRq.getName())) {
                throw new BusinessException("产品名称不能为空");
            }

            PProduct perz = doGet(modifyRq.getProductKey());
            if (perz == null) {
                throw new BusinessException("指定产品不存在或已删除，请确认产品密钥是否正确");
            }

            UpdateProductResponse updateProductResponse = productManager.modifyProduct(modifyRq);

            if (updateProductResponse.getBody().getSuccess()) {
                perz.setName(modifyRq.getName());
                if (!StringUtil.isNullOrBlank(modifyRq.getDescription())) {
                    perz.setDescription(modifyRq.getDescription());
                }
                if (!StringUtil.isNullOrBlank(modifyRq.getRemark())) {
                    perz.setRemark(modifyRq.getRemark());
                }
                perz.onModified(operator);

                productDao.save(perz);
            } else {
                throw new BusinessException("产品更新失败，原因：" + updateProductResponse.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) throws BusinessException {
        try {
            PProduct perz = doGet(id);
            if (perz == null) {
                return;
            }

            EquipmentFilter filter = new EquipmentFilter();
            filter.setProductKeyEq(perz.getProductKey());
            List<Equipment> equipmentList = equipmentService.query(filter).getData();
            if (!CollectionUtils.isEmpty(equipmentList)) {
                throw new BusinessException("当前产品下存在未删除的设备，请删除设备后重试！");
            }

            DeleteProductResponse deleteProductResponse = productManager.deleteProduct(perz.getProductKey());

            if (deleteProductResponse.getBody().getSuccess()) {
                productDao.remove(PProduct.class, perz.getUuid());
            } else {
                throw new BusinessException("产品删除失败，原因：" + deleteProductResponse.getBody().getErrorMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Product> query(ProductFilter filter) throws BusinessException {
        try {
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

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public void saveInflux(String measurement, Map<String, Object> fields,Map<String,String> tags) throws BusinessException {
        influxDao.saveMessage(measurement, fields,tags);
    }

    @Override
    public Object queryInflux(String measurement, String command) throws BusinessException {
        QueryResult message = influxDao.getMessage(measurement, command);
        return message;
    }

    private PProduct doGet(String id) throws Exception {
        PProduct perz = productDao.get(PProduct.class, "productKey", id);
        if (perz == null) {
            perz = productDao.get(PProduct.class, id);
        }

        return perz;
    }
}
