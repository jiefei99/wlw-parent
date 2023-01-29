package com.jike.wlw.service.equipment;

import com.geeker123.rumba.commons.api.response.ActionResult;
import com.geeker123.rumba.commons.paging.PagingResult;

public interface BaseEquipmentService {
//    ActionResult<Equipment> getBasic(String tenantId, EquipmentGetRq getRq);

    ActionResult<Equipment> getDetail(String tenantId, EquipmentGetRq getRq);

    ActionResult<String> create(String tenantId, EquipmentCreateRq createRq, String operator);

//    ActionResult<Void> modify(String tenantId, ProductModifyRq modifyRq, String operator);

    ActionResult<Void> delete(String tenantId, EquipmentGetRq getRq);

    ActionResult<Void> enable(String tenantId, EquipmentGetRq getRq);

    ActionResult<Void> disable(String tenantId, EquipmentGetRq getRq);

    ActionResult<Equipment> getStatus(String tenantId, EquipmentGetRq getRq);

    ActionResult<Equipment> getStatistics(String tenantId, EquipmentStatisticsQueryRq queryRq);

    PagingResult<Equipment> queryOTAModuleVersions(String tenantId, EquipmentOTAModuleVersionRq versionRq);

    PagingResult<Equipment> queryByStatus(String tenantId, EquipmentQueryByStatusRq queryRq);

    PagingResult<Equipment> queryByProductKey(String tenantId, EquipmentQueryByProductRq queryRq);

}
