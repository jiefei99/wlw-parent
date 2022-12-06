/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2020，所有权利保留。
 * <p>
 * 修改历史：
 * 2020/7/25 15:59 - chenpeisi - 创建。
 */
package com.jike.wlw.core.flowcode;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.DateUtil;
import com.geeker123.rumba.commons.util.coding.FlowCodeRuler;
import com.jike.wlw.dao.flowcode.FlowCodeDao;
import com.jike.wlw.service.flowcode.FlowCodeService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 流水号服务实现
 */
@ApiModel("流水号服务实现")
@Slf4j
@RestController
public class FlowCodeServiceImpl implements FlowCodeService {

    @Autowired
    private FlowCodeDao flowCodeDao;


    @Override
    public String next(String flowName, String prefix, int flowLength) throws BusinessException {
        int retry = 0;
        while (true) {
            try {
                return doNext(flowName, prefix + DateUtil.formatToDay3(new Date()), flowLength);
            } catch (Exception e) {
                retry++;
                if (retry >= 3)
                    throw new RuntimeException("生成流水出错。", e);
            }
        }
    }

    @Override
    public String nextWithoutTime(String flowName, String prefix, int flowLength) throws BusinessException {
        int retry = 0;
        while (true) {
            try {
                return doNext(flowName, prefix, flowLength);
            } catch (Exception e) {
                retry++;
                if (retry >= 3)
                    throw new RuntimeException("生成流水出错。", e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized String doNext(String flowName, String prefix, int flowLength)
            throws Exception {
        String startCode = flowCodeDao.getMaxCode(flowName, prefix);
        if (startCode != null) {
            startCode = startCode.substring(prefix.length());
            startCode = StringUtils.rightPad(startCode, flowLength, "0");
        } else {
            startCode = StringUtils.rightPad("0", flowLength, "0");
        }
        FlowCodeRuler fcr = new FlowCodeRuler(flowLength);
        String nextCode = prefix + fcr.flow(startCode);
        flowCodeDao.saveCode(flowName, nextCode);
        return nextCode;
    }
}