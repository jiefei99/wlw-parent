package com.jike.wlw.core.product.serverSubscription.consumerGroup.privatization;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.jike.wlw.common.StringRelevant;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.product.serverSubscription.consumerGroup.ConsumerGroupDao;
import com.jike.wlw.dao.product.serverSubscription.consumerGroup.PConsumerGroup;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroup;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupCreateRq;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupFilter;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.ConsumerGroupModifyRq;
import com.jike.wlw.service.product.serverSubscription.consumerGroup.privatization.PrivateConsumerGroupService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: ConsumerGroupServiceImpl
 * @Author RS
 * @Date: 2023/1/13 16:41
 * @Version 1.0
 */

@Slf4j
@RestController("ConsumerGroupServicePrivateImpl")
@ApiModel("私有化消费组实现")
public class PrivateConsumerGroupServiceImpl extends BaseService implements PrivateConsumerGroupService {
    @Autowired
    private ConsumerGroupDao consumerGroupDao;

    @Override
    public String create(String tenantId, ConsumerGroupCreateRq createRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(tenantId)){
            throw new BusinessException("租户不能为空！");
        }
        if (StringUtils.isBlank(createRq.getGroupName())) {
            throw new BusinessException("消费组名称不能为空！");
        }
        int nameSize = StringRelevant.calcStrLength(createRq.getGroupName());
        if (nameSize < 4 || nameSize > 30) {
            throw new BusinessException("消费组名称长度为4~30个字符，请重新输入！");
        }
        try {
            String groupId = StringRelevant.buildRandomString(26);
            PConsumerGroup consumerGroup = new PConsumerGroup();
            consumerGroup.setName(createRq.getGroupName());
            consumerGroup.setId(StringRelevant.buildId(20));
            consumerGroup.setTenantId(tenantId);
            PConsumerGroup group = doGet(tenantId, groupId);
            while (group != null) {
                groupId = StringRelevant.buildRandomString(26);
                group = doGet(tenantId, groupId);
            }
            consumerGroup.setId(groupId);
            consumerGroupDao.save(consumerGroup);
            return consumerGroup.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("创建消费组失败：" + e.getMessage());
        }
    }

    @Override
    public void modify(String tenantId, ConsumerGroupModifyRq modifyRq, String operator) throws BusinessException {
        if (StringUtils.isBlank(modifyRq.getGroupName())) {
            throw new BusinessException("消费组名称不能为空！");
        }
        if (StringUtils.isBlank(modifyRq.getGroupId())) {
            throw new BusinessException("消费组ID不能为空！");
        }
        int nameSize = StringRelevant.calcStrLength(modifyRq.getGroupName());
        if (nameSize < 4 || nameSize > 30) {
            throw new BusinessException("消费组名称长度为4~30个字符，请重新输入！");
        }
        //查询groupId是否在数据库
        try {
            PConsumerGroup group = doGet(tenantId, modifyRq.getGroupId());
            if (group == null) {
                throw new BusinessException("指定产品不存在或已删除，请确认产品密钥是否正确");
            }
            group.setName(modifyRq.getGroupName());
            consumerGroupDao.save(group);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("修改消费组失败：" + e.getMessage());
        }
        //插入数据库
    }

    @Override
    public void delete(String tenantId, String groupId, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        try {
            PConsumerGroup group = doGet(tenantId, groupId);
            if (group == null) {
                return;
            }

            // TODO: 2023/1/13 需要先删除关联订阅才可以删除消费组
            consumerGroupDao.remove(PConsumerGroup.class, group.getUuid());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("删除消费组失败：" + e.getMessage());
        }

    }

    @Override
    public void resetPosition(String tenantId, String groupId, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        //todo 后期根据消息处理，现在暂时不处理
    }

    @Override
    public ConsumerGroup get(String tenantId, String groupId, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        try {
            PConsumerGroup source = doGet(tenantId, groupId);
            ConsumerGroup target = new ConsumerGroup();
            BeanUtils.copyProperties(source, target);
            target.setGroupId(source.getId());
            target.setGroupName(source.getName());
            target.setCreateTime(source.getCreated().toString());
            return target;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("查询消费组失败：" + e.getMessage());
        }
    }

    @Override
    public PagingResult<ConsumerGroup> query(String tenantId, ConsumerGroupFilter filter) throws BusinessException {
        if (filter.getCurrentPage() < 1) {
            throw new BusinessException("指定显示返回结果中的第几页，最小值为1！");
        }
        if (filter.getPageSize() < 1 || filter.getPageSize() > 1000) {
            throw new BusinessException("指定返回结果中每页显示的消费组数量，最小值为1，最大值为1000！");
        }
        try {
            List<PConsumerGroup> list = consumerGroupDao.query(filter);
            long count = consumerGroupDao.getCount(filter);
            List<ConsumerGroup> result = new ArrayList<>();
            for (PConsumerGroup source : list) {
                ConsumerGroup target = new ConsumerGroup();
                BeanUtils.copyProperties(source, target);
                target.setGroupName(source.getName());
                target.setGroupId(source.getId());
                target.setCreateTime(source.getCreated().toString());
                result.add(target);
            }
            return new PagingResult<>(filter.getCurrentPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("查询消费组列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<ConsumerGroup> getStatus(String tenantId, String groupId, String iotInstanceId) throws BusinessException {
        if (StringUtils.isBlank(groupId)) {
            throw new BusinessException("消费组ID不能为空！");
        }
        //todo 暂时不做处理 后续根据时序数据库查询数据
        try {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("查询消费组状态失败：" + e.getMessage());
        }
    }

    private PConsumerGroup doGet(String tenantId, String paramValue) throws Exception {
        PConsumerGroup group = consumerGroupDao.get(PConsumerGroup.class, "id", paramValue, "tenantId", tenantId);
        return group;
    }
}


