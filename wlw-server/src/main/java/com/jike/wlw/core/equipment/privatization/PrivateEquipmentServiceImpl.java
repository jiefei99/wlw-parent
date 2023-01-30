package com.jike.wlw.core.equipment.privatization;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.config.fegin.FlowCodeFeignClient;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.equipment.EquipmentDao;
import com.jike.wlw.dao.equipment.PEquipment;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentFilter;
import com.jike.wlw.service.equipment.EquipmentGetRq;
import com.jike.wlw.service.equipment.EquipmentOTAModuleVersionRq;
import com.jike.wlw.service.equipment.EquipmentQueryByProductRq;
import com.jike.wlw.service.equipment.EquipmentQueryByStatusRq;
import com.jike.wlw.service.equipment.EquipmentStatisticsQueryRq;
import com.jike.wlw.service.equipment.EquipmentStatus;
import com.jike.wlw.service.equipment.privatization.PrivateEquipmentService;
import com.jike.wlw.service.flowcode.FlowCodeService;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel("阿里云设备服务实现")
public class PrivateEquipmentServiceImpl extends BaseService implements PrivateEquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;
    @Qualifier("flowCodeServiceImpl")
    @Autowired
    private FlowCodeService flowCodeService;

    @Override
    public ActionResult<Equipment> getDetail(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(getRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, getRq.getId());
            if (perz == null) {
                return ActionResult.fail("指定设备不存在或已删除");
            }
            Equipment result = conver(perz);

            return ActionResult.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<String> create(String tenantId, EquipmentCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getDeviceName())) {
                return ActionResult.fail("设备名称不能为空");
            }
            if (StringUtil.isNullOrBlank(createRq.getProductKey())) {
                return ActionResult.fail("设备密钥不能为空");
            }

            EquipmentFilter filter = new EquipmentFilter();
            filter.setTenantIdEq(tenantId);
            filter.setNameEq(createRq.getDeviceName());
            filter.setProductKeyEq(createRq.getProductKey());
            List<PEquipment> query = equipmentDao.query(filter);
            if (!CollectionUtils.isEmpty(query)) {
                return ActionResult.fail("所属产品下的设备名称重复，不允许新增");
            }

            PEquipment perz = new PEquipment();
            BeanUtils.copyProperties(createRq, perz);
            perz.setTenantId(tenantId);
            perz.setName(createRq.getDeviceName());
            perz.setStatus(EquipmentStatus.UNACTIVE.name());
            if (StringUtil.isNullOrBlank(createRq.getId())) {
                perz.setId(flowCodeService.next(PEquipment.class.getSimpleName(), "SB", 6));
            } else {
                perz.setId(createRq.getId());
            }
            perz.onCreated(operator);

            equipmentDao.save(perz);
            return ActionResult.ok(perz.getUuid());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult delete(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(getRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, getRq.getId());
            if (perz == null) {
                return ActionResult.ok();
            }

            equipmentDao.remove(perz);
            return ActionResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<Void> enable(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(getRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, getRq.getId());
            if (perz == null) {
                return ActionResult.fail("指定设备不存在或已删除");
            }
            perz.setStatus(EquipmentStatus.ONLINE.name());

            equipmentDao.save(perz);
            return ActionResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public ActionResult<Void> disable(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(getRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, getRq.getId());
            if (perz == null) {
                return ActionResult.fail("指定设备不存在或已删除");
            }
            perz.setStatus(EquipmentStatus.DISABLE.name());

            equipmentDao.save(perz);
            return ActionResult.ok();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Equipment> getStatus(String tenantId, EquipmentGetRq getRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(getRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, getRq.getId());
            if (perz == null) {
                return ActionResult.fail("指定设备不存在或已删除");
            }

            Equipment result = new Equipment();
            result.setId(perz.getId());
            result.setName(perz.getName());
            result.setStatus(EquipmentStatus.valueOf(perz.getStatus()));
            result.setTimestamp(perz.getTimestamp());

            return ActionResult.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public ActionResult<Equipment> getStatistics(String tenantId, EquipmentStatisticsQueryRq queryRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(queryRq.getId())) {
                return ActionResult.fail("设备编号不能为空");
            }
            PEquipment perz = doGet(tenantId, queryRq.getId());
            if (perz == null) {
                return ActionResult.fail("指定设备不存在或已删除");
            }

            Equipment result = new Equipment();
            result.setId(perz.getId());
            result.setName(perz.getName());
            result.setActiveCount(perz.getActiveCount());
            result.setDeviceCount(perz.getDeviceCount());
            result.setOnlineCount(perz.getOnlineCount());

            return ActionResult.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryOTAModuleVersions(String tenantId, EquipmentOTAModuleVersionRq versionRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(versionRq.getId()) && (StringUtil.isNullOrBlank(versionRq.getName()) || StringUtil.isNullOrBlank(versionRq.getProductKey()))) {
                throw new BusinessException("无法获取指定设备信息，请补充设备必要信息");
            }

            EquipmentFilter filter = new EquipmentFilter();
            filter.setTenantIdEq(tenantId);
            filter.setPage(versionRq.getCurrentPage());
            filter.setPageSize(versionRq.getPageSize());
            if (StringUtil.isNullOrBlank(versionRq.getId())) {
                filter.setIdEq(versionRq.getId());
            } else if (StringUtil.isNullOrBlank(versionRq.getProductKey()) && StringUtil.isNullOrBlank(versionRq.getName())) {
                filter.setProductKeyEq(versionRq.getProductKey());
                filter.setNameEq(versionRq.getName());
            }

            List<PEquipment> list = equipmentDao.query(filter);
            long count = equipmentDao.getCount(filter);
            if (CollectionUtils.isEmpty(list)) {
                return new PagingResult<>(versionRq.getCurrentPage(), versionRq.getPageSize(), list.size(), null);
            }
            List<Equipment> result = new ArrayList<>();
            for (PEquipment pEquipment : list) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(pEquipment, equipment);
                result.add(equipment);
            }

            return new PagingResult<>(versionRq.getCurrentPage(), versionRq.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryByStatus(String tenantId, EquipmentQueryByStatusRq queryRq) throws BusinessException {
        try {
            if (queryRq.getStatus() == null) {
                throw new BusinessException("设备状态不能为空");
            }

            EquipmentFilter filter = new EquipmentFilter();
            filter.setTenantIdEq(tenantId);
            filter.setPage(queryRq.getCurrentPage());
            filter.setPageSize(queryRq.getPageSize());
            if (StringUtil.isNullOrBlank(queryRq.getProductKey())) {
                filter.setProductKeyEq(queryRq.getProductKey());
            }
            if (StringUtil.isNullOrBlank(queryRq.getResourceGroupId())) {
                filter.setResourceGroupIdEq(queryRq.getResourceGroupId());
            }
            EquipmentStatus status = null;
            switch (queryRq.getStatus()) {
                case 0:
                    status = EquipmentStatus.UNACTIVE;
                    break;
                case 1:
                    status = EquipmentStatus.ONLINE;
                    break;
                case 3:
                    status = EquipmentStatus.OFFLINE;
                    break;
                case 8:
                    status = EquipmentStatus.DISABLE;
                    break;
            }
            filter.setStatusEq(status);

            List<PEquipment> list = equipmentDao.query(filter);
            long count = equipmentDao.getCount(filter);
            if (CollectionUtils.isEmpty(list)) {
                return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), list.size(), null);
            }
            List<Equipment> result = new ArrayList<>();
            for (PEquipment pEquipment : list) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(pEquipment, equipment);
                result.add(equipment);
            }

            return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> queryByProductKey(String tenantId, EquipmentQueryByProductRq queryRq) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(queryRq.getProductKey())) {
                throw new BusinessException("设备所属的产品密钥不能为空");
            }

            EquipmentFilter filter = new EquipmentFilter();
            filter.setTenantIdEq(tenantId);
            filter.setPage(queryRq.getCurrentPage());
            filter.setPageSize(queryRq.getPageSize());
            filter.setProductKeyEq(queryRq.getProductKey());


            List<PEquipment> list = equipmentDao.query(filter);
            long count = equipmentDao.getCount(filter);
            if (CollectionUtils.isEmpty(list)) {
                return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), list.size(), null);
            }
            List<Equipment> result = new ArrayList<>();
            for (PEquipment pEquipment : list) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(pEquipment, equipment);
                result.add(equipment);
            }

            return new PagingResult<>(queryRq.getCurrentPage(), queryRq.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private PEquipment doGet(String tenantId, String id) throws Exception {
        PEquipment perz = equipmentDao.get(PEquipment.class, "tenantId", tenantId, "id", id);
        if (perz == null) {
            perz = equipmentDao.get(PEquipment.class, id);
        }
        return perz;
    }

    private PEquipment conver(Equipment source) {
        if (source == null) {
            return null;
        }
        PEquipment target = new PEquipment();
        BeanUtils.copyProperties(source, target);
        target.setStatus(source.getStatus().name());

        return target;
    }


    private Equipment conver(PEquipment source) {
        if (source == null) {
            return null;
        }
        Equipment target = new Equipment();
        BeanUtils.copyProperties(source, target);
        target.setStatus(EquipmentStatus.valueOf(source.getStatus()));

        return target;
    }
}
