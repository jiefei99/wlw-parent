package com.jike.wlw.core.management.access;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.management.access.AccessRecordDao;
import com.jike.wlw.dao.management.access.PAccessRecord;
import com.jike.wlw.service.management.access.AccessRecord;
import com.jike.wlw.service.management.access.AccessRecordFilter;
import com.jike.wlw.service.management.access.AccessRecordService;
import com.jike.wlw.service.management.access.AccessRecordStatus;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApiModel("访问记录实现")
@RestController
public class AccessRecordServiceImpl extends BaseService implements AccessRecordService {

    @Autowired
    private AccessRecordDao accessRecordDao;

    @Override
    public void add(AccessRecord accessRecord) throws BusinessException {
        try {
            PAccessRecord perz = new PAccessRecord();
            perz.onCreated(accessRecord.getLoginUserName());
            perz.setIp(accessRecord.getIp());
            if (accessRecord.getStatus() != null) {
                perz.setStatus(accessRecord.getStatus().name());
            }
            perz.setAction(accessRecord.getAction());
            perz.setLoginUserId(accessRecord.getLoginUserId());
            perz.setLoginUserName(accessRecord.getLoginUserName());

            accessRecordDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<AccessRecord> query(AccessRecordFilter filter) throws BusinessException {
        try {
            List<PAccessRecord> list = accessRecordDao.query(filter);
            long total = accessRecordDao.getCount(filter);

            List<AccessRecord> result = new ArrayList<>();
            for (PAccessRecord perz : list) {
                AccessRecord accessRecord = new AccessRecord();
                BeanUtils.copyProperties(perz, accessRecord);
                if (!StringUtil.isNullOrBlank(perz.getStatus())) {
                    accessRecord.setStatus(AccessRecordStatus.valueOf(perz.getStatus()));
                }
                result.add(accessRecord);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), total, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

}
