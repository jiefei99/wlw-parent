package com.jike.wlw.core.physicalmodel.privatization;

import com.alibaba.fastjson.JSON;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.dao.physicalmodel.PPhysicalModelModule;
import com.jike.wlw.dao.physicalmodel.PhysicalModelModuleDao;
import com.jike.wlw.dao.physicalmodel.PhysicalModelFunctionDao;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelDelRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelGetRq;
import com.jike.wlw.service.physicalmodel.PhysicalModelModifyRq;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelFunctionService;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelManagerService;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionDelRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.PhysicalModelFunctionModifyRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.function.Model;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModule;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModuleCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModuleFilter;
import com.jike.wlw.service.physicalmodel.privatization.pojo.module.PhysicalModelModuleModifyRq;
import com.jike.wlw.service.product.info.privatization.PrivateProductService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    private PhysicalModelModuleDao modelModuleDao;
    @Autowired
    private PhysicalModelFunctionService functionService;


    @Override
    @Transactional
    public void create(String tenantId, PhysicalModelCreateRq createRq, String operator) throws BusinessException {
        if (createRq==null){
            throw new BusinessException("物模型参数不能为空");
        }
        if (StringUtil.isNullOrBlank(createRq.getProductKey())) {
            throw new BusinessException("物模型所属productKey不能为空");
        }
        //创建相应的属性、事件、服务
        if (StringUtils.isBlank(createRq.getThingModelJson())){
            throw new BusinessException("物模型模块功能不能为空");
        }
        try {
            //判断是否需要创建模块
            if (productService.get(tenantId,createRq.getProductKey(),null)==null){
                throw new BusinessException("产品不存在，请重新选择");
            }
            PPhysicalModelModule modelModule = doGetModule(tenantId, createRq.getProductKey(), createRq.getFunctionBlockId());
            if (modelModule==null){
                throw new BusinessException("产品物模型模块不存在");
            }
            PhysicalModelFunctionCreateRq functionCreateRq = JSON.parseObject(createRq.getThingModelJson(), PhysicalModelFunctionCreateRq.class);
            functionCreateRq.setModelModuleId(modelModule.getUuid());
            functionCreateRq.setProductKey(createRq.getProductKey());
            functionService.create(tenantId,functionCreateRq,operator);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public Model get(String tenantId, PhysicalModelGetRq modelGetRq) throws BusinessException {
        if (modelGetRq==null){
            throw new BusinessException("查询物模型参数不能为空");
        }
        if (StringUtil.isNullOrBlank(modelGetRq.getProductKey())) {
            throw new BusinessException("物模型所属productKey不能为空");
        }
        if (StringUtils.isBlank(modelGetRq.getFunctionBlockId())){
            throw new BusinessException("物模型模块标识符不能为空");
        }
        if (StringUtils.isBlank(modelGetRq.getIdentifier())){
            throw new BusinessException("物模型标识符不能为空");
        }
        Model model = functionService.get(tenantId, modelGetRq.getProductKey(), modelGetRq.getFunctionBlockId(), modelGetRq.getIdentifier());
        return model;
    }

    @Override
    public void modify(String tenantId, PhysicalModelModifyRq modifyRq, String operator) throws BusinessException {
        if (modifyRq==null){
            throw new BusinessException("修改物模型参数不能为空");
        }
        if (StringUtil.isNullOrBlank(modifyRq.getProductKey())) {
            throw new BusinessException("物模型所属productKey不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getFunctionBlockId())){
            throw new BusinessException("物模型模块标识符不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getIdentifier())){
            throw new BusinessException("物模型标识符不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getThingModelJson())){
            throw new BusinessException("物模型标识符不能为空");
        }
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, modifyRq.getProductKey(), modifyRq.getFunctionBlockId());
            if (modelModule==null){
                throw new BusinessException("产品物模型模块不存在");
            }
            PhysicalModelFunctionModifyRq functionModifyRq = JSON.parseObject(modifyRq.getThingModelJson(), PhysicalModelFunctionModifyRq.class);
            functionModifyRq.setModelModuleId(modelModule.getUuid());
            functionService.modify(tenantId,functionModifyRq,operator);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void delete(String tenantId, PhysicalModelDelRq modelDelRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (modelDelRq==null){
            throw new BusinessException("物模型删除条件不能为空");
        }
        if (StringUtil.isNullOrBlank(modelDelRq.getProductKey())) {
            throw new BusinessException("物模型所属productKey不能为空");
        }
        if (CollectionUtils.isEmpty(modelDelRq.getServiceIdentifier())&&
            CollectionUtils.isEmpty(modelDelRq.getEventIdentifier())&&
            CollectionUtils.isEmpty(modelDelRq.getPropertyIdentifier())){
            throw new BusinessException("物模型要删除的功能不能为空");
        }
        try {
            PhysicalModelFunctionDelRq functionDelRq=new PhysicalModelFunctionDelRq();
            functionDelRq.setProductKey(modelDelRq.getProductKey());
            functionDelRq.setModuleIdentifier(StringUtils.isNotBlank(modelDelRq.getFunctionBlockId())?modelDelRq.getFunctionBlockId():"default");
            List<String> identifierList =new ArrayList<>();
            if (CollectionUtils.isEmpty(modelDelRq.getServiceIdentifier())){
                identifierList.addAll(modelDelRq.getServiceIdentifier());
            }
            if (CollectionUtils.isEmpty(modelDelRq.getEventIdentifier())){
                identifierList.addAll(modelDelRq.getEventIdentifier());
            }
            if (CollectionUtils.isEmpty(modelDelRq.getPropertyIdentifier())){
                identifierList.addAll(modelDelRq.getPropertyIdentifier());
            }
            functionDelRq.setIdentifierIn(identifierList);
            functionService.delete(tenantId,functionDelRq,operator);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public String createModule(String tenantId, PhysicalModelModuleCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (createRq==null){
            throw new BusinessException("物模型模块新建条件不能为空");
        }
        if (StringUtils.isBlank(createRq.getIdentifier())){
            throw new BusinessException("模块标识符不能为空");
        }
        if (StringUtils.isBlank(createRq.getProductKey())){
            throw new BusinessException("产品productKey不能为空");
        }
        if (StringUtils.isBlank(createRq.getName())){
            throw new BusinessException("模块名称不能为空");
        }
        Long count = checkModelMoule(tenantId, createRq.getProductKey(), createRq.getName());
        if (count>0){
            throw new BusinessException("模块名称不能重复");
        }
        //保存
        PPhysicalModelModule modelModule=new PPhysicalModelModule();
        try {
            PhysicalModelModule module = getModule(tenantId, createRq.getProductKey(), createRq.getIdentifier());
            if (module!=null){
                throw new BusinessException("模块已存在");
            }
            modelModule.setDetails(createRq.getDetails());
            modelModule.setIdentifier(createRq.getIdentifier());
            modelModule.setName(createRq.getName());
            modelModule.setTenantId(tenantId);
            modelModule.onCreated(operator);
            modelModule.setProductKey(createRq.getProductKey());
            modelModuleDao.save(modelModule);
            return modelModule.getUuid();
        } catch (Exception e) {
            throw new BusinessException("模块保存失败："+e.getMessage());
        }
    }

    @Override
    public void modifyModule(String tenantId, PhysicalModelModuleModifyRq modifyRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (modifyRq==null){
            throw new BusinessException("物模型模块修改条件不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getIdentifier())){
            throw new BusinessException("模块标识符不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getProductKey())){
            throw new BusinessException("产品productKey不能为空");
        }
        if (StringUtils.isBlank(modifyRq.getName())){
            throw new BusinessException("模块名称不能为空");
        }
        Long count = checkModelMoule(tenantId, modifyRq.getProductKey(), modifyRq.getName());
        if (count>0){
            throw new BusinessException("模块名称不能重复");
        }
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, modifyRq.getProductKey(), modifyRq.getIdentifier());
            if (modelModule==null){
                throw new BusinessException("产品物模型模块不存在");
            }
            modelModule.setName(modifyRq.getName());
            modelModule.setDetails(modifyRq.getDetails());
            modelModule.onModified(operator);
            modelModuleDao.save(modelModule);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public void deleteModule(String tenantId, String productKey, String identifier, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (StringUtils.isBlank(identifier)){
            throw new BusinessException("模块标识符不能为空");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("产品productKey不能为空");
        }
        try {
            PPhysicalModelModule modelModule = doGetModule(tenantId, productKey, identifier);
            if (modelModule.isPublished()){
                throw new BusinessException("物模型模块已发布，不可删除");
            }
            modelModule.onModified(operator);
            modelModule.setIsDeleted(1);
            modelModuleDao.save(modelModule);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<PhysicalModelModule> queryModule(String tenantId, String productKey) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("产品productKey不能为空");
        }
        PhysicalModelModuleFilter filter=new PhysicalModelModuleFilter();
        filter.setTenantId(tenantId);
        filter.setProductKey(productKey);
        List<PPhysicalModelModule> query = modelModuleDao.query(filter);
        List<PhysicalModelModule> physicalModelModuleList=new ArrayList<>();
        if (CollectionUtils.isEmpty(query)){
            return physicalModelModuleList;
        }
        for (PPhysicalModelModule pModelModule : query) {
            PhysicalModelModule modelModule = new PhysicalModelModule();
            BeanUtils.copyProperties(pModelModule,modelModule);
            physicalModelModuleList.add(modelModule);
        }
        return physicalModelModuleList;
    }

    @Override
    public PhysicalModelModule getModule(String tenantId, String productKey, String identifier) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空");
        }
        if (StringUtils.isBlank(productKey)){
            throw new BusinessException("产品productKey不能为空");
        }
        if (StringUtils.isBlank(identifier)){
            throw new BusinessException("模块标识符不能为空");
        }
        PhysicalModelModule modelModule = new PhysicalModelModule();
        try {
            PPhysicalModelModule pModelModule = doGetModule(tenantId, productKey, identifier);
            BeanUtils.copyProperties(pModelModule,modelModule);
            return modelModule;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    private Long checkModelMoule(String tenantId,String productKey,String name){
        PhysicalModelModuleFilter filter=new PhysicalModelModuleFilter();
        filter.setTenantId(tenantId);
        filter.setNameEq(name);
        filter.setProductKey(productKey);
        long count = modelModuleDao.getCount(filter);
        return count;
    }

    private PPhysicalModelModule doGetModule (String tenantId,String productKey,String identifier) throws Exception {
        PPhysicalModelModule perz = modelModuleDao.get(PPhysicalModelModule.class, "productKey", productKey,"identifier",identifier,"tenantId",tenantId,"isDeleted",0);
        return perz;
    }
}


