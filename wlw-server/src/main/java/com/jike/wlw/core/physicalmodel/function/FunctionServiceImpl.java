package com.jike.wlw.core.physicalmodel.function;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.config.fegin.FlowCodeFeignClient;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.physicalmodel.function.FunctionDao;
import com.jike.wlw.dao.physicalmodel.function.PFunction;
import com.jike.wlw.service.physicalmodel.function.AccessMode;
import com.jike.wlw.service.physicalmodel.function.Function;
import com.jike.wlw.service.physicalmodel.function.FunctionCreateRq;
import com.jike.wlw.service.physicalmodel.function.FunctionData;
import com.jike.wlw.service.physicalmodel.function.FunctionFilter;
import com.jike.wlw.service.physicalmodel.function.FunctionModifyRq;
import com.jike.wlw.service.physicalmodel.function.FunctionService;
import com.jike.wlw.service.physicalmodel.function.FunctionType;
import com.jike.wlw.service.physicalmodel.function.ValueType;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel("功能服务实现")
public class FunctionServiceImpl extends BaseService implements FunctionService {

    @Autowired
    private FunctionDao functionDao;
    @Autowired
    private FlowCodeFeignClient flowCodeFeignClient;

    @Override
    public Function get(String tenantId, String id) throws BusinessException {
        try {
            PFunction perz = doGet(tenantId, id);
            if (perz == null) {
                return null;
            }

            Function result = new Function();
            BeanUtils.copyProperties(perz, result);
            result.setType(FunctionType.valueOf(perz.getType()));
            result.setAccessMode(AccessMode.valueOf(perz.getAccessMode()));
            if (FunctionType.PROPERTY.name().equals(perz.getType())) {
                result.setValueType(ValueType.valueOf(perz.getValueType()));
                result.setValue(JsonUtil.jsonToObject(perz.getValueJson(), Object.class));
            } else if (FunctionType.EVENT.name().equals(perz.getType()) || FunctionType.SERVE.name().equals(perz.getType())) {
                result.setOutputData(JsonUtil.jsonToObject(perz.getOutputDataJson(), FunctionData.class));
                result.setInputData(JsonUtil.jsonToObject(perz.getInputDataJson(), FunctionData.class));
            } else {
                throw new BusinessException("未知的功能类型");
            }

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void create(String tenantId, FunctionCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getName())) {
                throw new BusinessException("功能名称不能为空");
            }
            if (createRq.getAccessMode() == null) {
                throw new BusinessException("功能读写类型不能为空");
            }
            if (createRq.getType() == null) {
                throw new BusinessException("功能类型不能为空");
            } else {
                if (FunctionType.PROPERTY.equals(createRq.getType())) {
                    if (createRq.getValueType() == null) {
                        throw new BusinessException("属性类功能的期望值类型不能为空");
                    }
                } else if (FunctionType.EVENT.name().equals(createRq.getType()) || FunctionType.SERVE.name().equals(createRq.getType())) {
                    if (StringUtil.isNullOrBlank(createRq.getDesc())) {
                        throw new BusinessException("事件/服务类功能的描述不能为空");
                    }
                    if (createRq.getOutputData() == null) {
                        throw new BusinessException("功能出参不能为空");
                    }
                    if (FunctionType.EVENT.name().equals(createRq.getType()) && createRq.getInputData() == null) {
                        throw new BusinessException("功能入参不能为空");
                    }
                } else {
                    throw new BusinessException("未知的功能类型");
                }
            }

            PFunction perz = new PFunction();
            perz.setId(flowCodeFeignClient.next(PFunction.class.getSimpleName(), "GN", 6));
            perz.setTenantId(tenantId);
            BeanUtils.copyProperties(createRq, perz);
            perz.setType(createRq.getType().name());
            perz.setAccessMode(createRq.getAccessMode().name());
            if (createRq.getValueType() != null) {
                perz.setValueType(createRq.getValueType().name());
            }
            if (createRq.getValue() != null) {
                perz.setValueJson(JsonUtil.objectToJson(createRq.getValue()));
            }
            if (createRq.getOutputData() != null) {
                perz.setOutputDataJson(JsonUtil.objectToJson(createRq.getOutputData()));
            }
            if (createRq.getInputData() != null) {
                perz.setInputDataJson(JsonUtil.objectToJson(createRq.getInputData()));
            }

            functionDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(String tenantId, FunctionModifyRq modifyRq, String operator) throws BusinessException {
        try {
            PFunction perz = doGet(tenantId, modifyRq.getUuid());
            if (perz == null) {
                throw new BusinessException("指定功能不存在或已删除");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (modifyRq.getAccessMode() != null) {
                perz.setAccessMode(modifyRq.getAccessMode().name());
            }
            if (modifyRq.getRequired() != null) {
                perz.setRequired(modifyRq.getRequired());
            }

            functionDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Function> query(String tenantId, FunctionFilter filter) throws BusinessException {
        try {
            filter.setTenantIdEq(tenantId);
            List<PFunction> list = functionDao.query(filter);
            long count = functionDao.getCount(filter);

            List<Function> result = new ArrayList<>();
            for (PFunction perz : list) {
                Function function = new Function();
                BeanUtils.copyProperties(perz, function);
                function.setType(FunctionType.valueOf(perz.getType()));
                function.setAccessMode(AccessMode.valueOf(perz.getAccessMode()));
                if (FunctionType.PROPERTY.name().equals(perz.getType())) {
                    function.setValueType(ValueType.valueOf(perz.getValueType()));
                    function.setValue(JsonUtil.jsonToObject(perz.getValueJson(), Object.class));
                } else if (FunctionType.EVENT.name().equals(perz.getType()) || FunctionType.SERVE.name().equals(perz.getType())) {
                    function.setOutputData(JsonUtil.jsonToObject(perz.getOutputDataJson(), FunctionData.class));
                    function.setInputData(JsonUtil.jsonToObject(perz.getInputDataJson(), FunctionData.class));
                } else {
                    throw new BusinessException("未知的功能类型，功能编号:" + perz.getUuid());
                }

                result.add(function);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private PFunction doGet(String tenantId, String id) throws Exception {
        PFunction perz = functionDao.get(PFunction.class, "tenantId", tenantId, "id", id);
        if (perz == null) {
            perz = functionDao.get(PFunction.class, "tenantId", tenantId, "uuid", id);
        }

        return perz;
    }
}
