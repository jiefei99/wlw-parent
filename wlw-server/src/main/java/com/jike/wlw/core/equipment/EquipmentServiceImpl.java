package com.jike.wlw.core.equipment;

import com.aliyuncs.iot.model.v20180120.DeleteDeviceResponse;
import com.aliyuncs.iot.model.v20180120.RegisterDeviceResponse;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.paging.PagingResult;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.config.fegin.FlowCodeFeignClient;
import com.jike.wlw.core.BaseService;
import com.jike.wlw.core.equipment.iot.IemEquipmentManager;
import com.jike.wlw.dao.TX;
import com.jike.wlw.dao.equipment.EquipmentDao;
import com.jike.wlw.dao.equipment.PEquipment;
import com.jike.wlw.service.equipment.Equipment;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.EquipmentFilter;
import com.jike.wlw.service.equipment.EquipmentModifyRq;
import com.jike.wlw.service.equipment.EquipmentService;
import com.jike.wlw.service.equipment.EquipmentStatus;
import com.jike.wlw.service.equipment.MQTTConnection;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@ApiModel("设备服务实现")
public class EquipmentServiceImpl extends BaseService implements EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private FlowCodeFeignClient flowCodeFeignClient;
    @Autowired
    private IemEquipmentManager equipmentManager;

    @Override
    public Equipment get(String id) throws BusinessException {
        try {
            PEquipment perz = doGet(id);
            if (perz == null) {
                return null;
            }

            Equipment result = new Equipment();
            BeanUtils.copyProperties(perz, result);
            result.setStatus(EquipmentStatus.valueOf(perz.getStatus()));
            result.setConnectMQTT(JsonUtil.jsonToObject(perz.getConnectMQTTJson(), MQTTConnection.class));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void create(EquipmentCreateRq createRq, String operator) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(createRq.getProductKey())) {
                throw new BusinessException("产品密钥不能为空");
            }
//            if (StringUtil.isNullOrBlank(createRq.getDeviceSecret())) {
//                throw new BusinessException("设备密钥不能为空");
//            }

            PEquipment perz = new PEquipment();
            String equipmentId;
            if (StringUtil.isNullOrBlank(createRq.getId())) {
                equipmentId = flowCodeFeignClient.next(PEquipment.class.getSimpleName(), "SB", 6);
            } else {
                equipmentId = createRq.getId();
            }
            //注册IOT
            RegisterDeviceResponse.Data lotData = equipmentManager.registerDevice(createRq.getProductKey(), equipmentId);  //productKey(必填), deviceName(非必填)
            perz.setProductKey(lotData.getProductKey());
            perz.setDeviceSecret(lotData.getDeviceSecret());
            //---------------------------
            BeanUtils.copyProperties(createRq, perz);
            if (createRq.getConnectMQTT() != null) {
                perz.setConnectMQTTJson(JsonUtil.objectToJson(createRq.getConnectMQTT())); //暂时没搞懂有啥用
            }
            perz.setStatus(EquipmentStatus.INACTIVE.name());
            perz.onCreated(operator);

            equipmentDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void modify(EquipmentModifyRq modifyRq, String operator) throws BusinessException {
        try {
            PEquipment perz = doGet(modifyRq.getId());
            if (perz == null) {
                throw new BusinessException("指定设备不存在或已删除");
            }

            if (!StringUtil.isNullOrBlank(modifyRq.getName())) {
                perz.setName(modifyRq.getName());
            }
            if (modifyRq.getStatus() != null) {
                perz.setStatus(modifyRq.getStatus().name());
            }
            if (modifyRq.getConnectMQTT() != null) {
                perz.setConnectMQTTJson(JsonUtil.objectToJson(modifyRq.getConnectMQTT()));
            }
            perz.onModified(operator);

            equipmentDao.save(perz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @TX
    @Override
    public void delete(String id) throws BusinessException {
        try {
            PEquipment perz = doGet(id);
            if (perz == null) {
                return;
            }

            DeleteDeviceResponse deleteDeviceResponse = equipmentManager.deleteDevice(null, perz.getProductKey(), perz.getId());

            if (!deleteDeviceResponse.getSuccess()) {
                throw new BusinessException("删除设备失败，原因：" + deleteDeviceResponse.getErrorMessage());
            } else {
                //先做物理删除，后期需要判断是做物理删除还是逻辑删除
                equipmentDao.remove(PEquipment.class, perz.getUuid());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @Override
    public PagingResult<Equipment> query(EquipmentFilter filter) throws BusinessException {
        try {
            List<PEquipment> list = equipmentDao.query(filter);
            long count = equipmentDao.getCount(filter);

            List<Equipment> result = new ArrayList<>();
            for (PEquipment perz : list) {
                Equipment equipment = new Equipment();
                BeanUtils.copyProperties(perz, equipment);
                equipment.setStatus(EquipmentStatus.valueOf(perz.getStatus()));
                equipment.setConnectMQTT(JsonUtil.jsonToObject(perz.getConnectMQTTJson(), MQTTConnection.class));

                result.add(equipment);
            }

            return new PagingResult<>(filter.getPage(), filter.getPageSize(), count, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
    }

    private PEquipment doGet(String id) throws Exception {
        PEquipment perz = equipmentDao.get(PEquipment.class, "id", id);
        if (perz == null) {
            perz = equipmentDao.get(PEquipment.class, id);
        }

        return perz;
    }
}
