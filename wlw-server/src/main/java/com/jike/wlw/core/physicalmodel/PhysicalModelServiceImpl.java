package com.jike.wlw.core.physicalmodel;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.config.fegin.FlowCodeFeignClient;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.physicalmodel.PPhysicalModel;
import com.jike.wlw.dao.physicalmodel.PhysicalModelDao;
import com.jike.wlw.service.physicalmodel.PhysicalModel;
import com.jike.wlw.service.physicalmodel.PhysicalModelFilter;
import com.jike.wlw.service.physicalmodel.PhysicalModelService;
import com.jike.wlw.service.physicalmodel.function.Function;
import com.jike.wlw.service.physicalmodel.function.FunctionFilter;
import com.jike.wlw.service.physicalmodel.function.FunctionService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel("物模型服务实现")
public class PhysicalModelServiceImpl extends BaseService implements PhysicalModelService {

    @Autowired
    private PhysicalModelDao physicalModelDao;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private FlowCodeFeignClient flowCodeFeignClient;

    @Override
    public PhysicalModel get(String tenantId, String id) throws BusinessException {
        try {
            PPhysicalModel perz = doGet(tenantId, id);
            if (perz == null) {
                return null;
            }

            PhysicalModel result = new PhysicalModel();
            BeanUtils.copyProperties(perz, result);
            //应该要删掉的 TODO
//            result.setFunctionIds(JsonUtil.jsonToArrayList(perz.getFunctionIdsJson(), String.class));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void create(String tenantId, PhysicalModel createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("物模型名称不能为空");
            }
            if (CollectionUtils.isEmpty(createRq.getFunctionIds())) {
                throw new BusinessException("功能编号集合不能为空");
            }

            //校验功能是否存在
            FunctionFilter filter = new FunctionFilter();
            filter.setIdIn(createRq.getFunctionIds());
            List<Function> functionList = functionService.query(tenantId, filter).getData();
            if (functionList.size() != createRq.getFunctionIds().size()) {
                throw new BusinessException("部分功能不存在或已删除，无法添加");
            }

            PPhysicalModel perz = new PPhysicalModel();
            BeanUtils.copyProperties(createRq, perz);
            perz.setTenantId(tenantId);
//            perz.setId(flowCodeFeignClient.next(PPhysicalModel.class.getSimpleName(), "WMX", 6));
            //应该要删掉的 TODO
//            perz.setFunctionIdsJson(JsonUtil.objectToJson(createRq.getFunctionIds()));

            physicalModelDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, PhysicalModel modifyRq, String operator) throws BusinessException {
        try {
            PPhysicalModel perz = doGet(tenantId, modifyRq.getId());
            if (perz == null) {
                throw new BusinessException("指定物模型不存在或已删除，无法编辑");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setModelName(modifyRq.getName());
            }
            if (!CollectionUtils.isEmpty(modifyRq.getFunctionIds())) {
                //校验功能是否存在
                FunctionFilter filter = new FunctionFilter();
                filter.setIdIn(modifyRq.getFunctionIds());
                List<Function> functionList = functionService.query(tenantId, filter).getData();
                if (functionList.size() != modifyRq.getFunctionIds().size()) {
                    throw new BusinessException("部分功能不存在或已删除，无法添加");
                }
                //这个设计再考虑一下。应该是整块代码都要删掉的 TODO
//                perz.setFunctionIdsJson(JsonUtil.objectToJson(modifyRq.getFunctionIds()));
            }

            physicalModelDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<PhysicalModel> query(String tenantId, PhysicalModelFilter filter) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PPhysicalModel> list = physicalModelDao.query(filter);
            long count = physicalModelDao.getCount(filter);

            List<PhysicalModel> result = new ArrayList<>();
            for (PPhysicalModel perz : list) {
                PhysicalModel physicalModel = new PhysicalModel();
                BeanUtils.copyProperties(perz, physicalModel);
                //应该要删掉的 TODO
//                physicalModel.setFunctionIds(JsonUtil.jsonToArrayList(perz.getFunctionIdsJson(), String.class));

                result.add(physicalModel);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }


    private PPhysicalModel doGet(String tenantId, String id) throws Exception {
        PPhysicalModel perz = physicalModelDao.get(PPhysicalModel.class, "tenantId", tenantId, "id", id);
        if (perz == null) {
            perz = physicalModelDao.get(PPhysicalModel.class, "tenantId", tenantId, "uuid", id);
        }

        return perz;
    }
}
