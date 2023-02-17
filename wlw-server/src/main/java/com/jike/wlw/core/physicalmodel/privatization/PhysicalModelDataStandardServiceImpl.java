package com.jike.wlw.core.physicalmodel.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.jike.wlw.service.physicalmodel.PhysicalModelCreateRq;
import com.jike.wlw.service.physicalmodel.privatization.PhysicalModelDataStandardService;
import com.jike.wlw.service.physicalmodel.privatization.entity.PhysicalModelDataStandardCreateRq;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: PhysicalModelDataStandardServiceImpl
 * @Author RS
 * @Date: 2023/2/17 19:46
 * @Version 1.0
 */

@Slf4j
@RestController("modelDataStandardServicePrivateImpl")
@ApiModel("私有化物模型数据标准实现")
public class PhysicalModelDataStandardServiceImpl implements PhysicalModelDataStandardService {
    @Override
    public void create(String tenantId, PhysicalModelDataStandardCreateRq createRq, String operator) throws BusinessException {

    }
}


