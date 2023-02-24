package com.jike.wlw.core.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModel;
import com.jike.wlw.dao.physicalmodel.PhysicalModelDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelEventDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelPropertyDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelManagerService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.product.info.privatization.PrivateProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: PhysicalModelManagerServiceImpl
 * @Author RS
 * @Date: 2023/2/16 17:23
 * @Version 1.0
 */

@Slf4j
@RestController("modelServicePrivateImpl")
@ApiModel("私有化物模型服务实现")
public class PhysicalModelManagerServiceImpl implements PhysicalModelManagerService {
    @Autowired
    private PrivateProductService productService;
    @Autowired
    private PhysicalModelDao modelDao;
    @Autowired
    private PhysicalModelFunctionService functionService;
    @Autowired
    private PhysicalModelFunctionDao serviceDao;
    @Autowired
    private PhysicalModelPropertyDao propertyDao;


    @Override
    @Transactional
    public void create(String tenantId, PhysicalModelCreateRq createRq, String operator) throws BusinessException {
        if (createRq==null){
            throw new BusinessException("物模型参数不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getProductKey())) {
            throw new BusinessException("物模型所属productKey不能为空");
        }
        if (StringUtils.isBlank(createRq.getFunctionBlockId())&&StringUtils.isBlank(createRq.getFunctionBlockName())){
            throw new BusinessException("物模型所属名称与标识符不能为空");
        }
        PPhysicalModel pPhysicalModel=new PPhysicalModel();
        try {
            if (productService.get(tenantId,createRq.getProductKey(),null)==null){
                throw new BusinessException("产品不存在，请重新选择");
            }
            pPhysicalModel.setModelIdentifier(createRq.getFunctionBlockId());
            pPhysicalModel.setModelName(createRq.getFunctionBlockName());
            pPhysicalModel.setProductKey(createRq.getProductKey());
            pPhysicalModel.setDetails(createRq.getDetails());
            pPhysicalModel.setTenantId(tenantId);
            pPhysicalModel.onCreated(operator);
            modelDao.save(pPhysicalModel);
            //创建相应的属性、事件、服务
            if (StringUtils.isNotBlank(createRq.getThingModelJson())){
                PhysicalModelFunctionCreateRq functionCreateRq = new PhysicalModelFunctionCreateRq();
                functionCreateRq.setModelDeviceId(pPhysicalModel.getUuid());
                functionCreateRq.setThingModelJson(createRq.getThingModelJson());
                functionService.create(tenantId,functionCreateRq,operator);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    @Override
    public PhysicalModel get(String tenantId, PhysicalModelGetRq modelGetRq) throws BusinessException {
        return null;
    }

    @Override
    public void modify(String tenantId, PhysicalModelModifyRq modifyRq, String operator) throws BusinessException {

    }

    @Override
    public void delete(String tenantId, PhysicalModelDelRq modelDelRq, String operator) throws BusinessException {

    }

}


