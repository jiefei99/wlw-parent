package com.jike.wlw.core.equipment.ali.imp;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.JsonUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.oss.CloudStorageConfig;
import com.jike.wlw.common.ImportData;
import com.jike.wlw.core.support.excel.ExcelBean;
import com.jike.wlw.core.support.excel.ExcelHelper;
import com.jike.wlw.core.support.excel.ExcelWriter;
import com.jike.wlw.service.config.Config;
import com.jike.wlw.service.config.ConfigService;
import com.jike.wlw.service.equipment.EquipmentCreateRq;
import com.jike.wlw.service.equipment.ali.AliEquipmentService;
import com.jike.wlw.service.support.oss.AliyunOSSConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.jike.wlw.common.Constants.Config.GROUP_OSS_WLW;
import static com.jike.wlw.common.Constants.Config.KEY_OSS_WLW_CONFIG_KEY;

/**
 * @author mengchen
 * @date 2022/8/31
 * @apiNote
 */
@Slf4j
@Service
public class EquipmentImporter {
    @Autowired
    private AliEquipmentService aliEquipmentService;
    @Autowired
    private ConfigService configService;

    private List<ExcelImportDevice> records = new ArrayList<>();

    public ImportData doImport(String tenantId, String productKey, String filePath) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(filePath)) {
                throw new BusinessException("文件路径不能为空");
            }

            // 读取文件
            String fileName = null;
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            readRecord(filePath);

            // 验证导入信息
            ImportData importData = validateImportRecords();

            // 开始导入
            doImport(tenantId, productKey);

            for (ExcelBean result : records) {
                switch (result.getImportResult()) {
                    case ExcelBean.IMPRESULT_FAILED:
                        importData.setFailCount(importData.getFailCount() + 1);
                        break;
                    case ExcelBean.IMPRESULT_IGNORE:
                        importData.setIgnoreCount(importData.getIgnoreCount() + 1);
                        break;
                    default:
                        importData.setSuccessCount(importData.getSuccessCount() + 1);
                        break;
                }
            }
            // 回写导入结果
            String backFileName = ExcelHelper.generateBackFile(filePath, fileName);
            List<String> filePathNodes = new ArrayList<String>();
            filePathNodes.add("back");

            AliyunOSSConfig aliyunOSSConfig = new AliyunOSSConfig();
            Config config = configService.get(GROUP_OSS_WLW, KEY_OSS_WLW_CONFIG_KEY);
            CloudStorageConfig cloudStorageConfig = JsonUtil.jsonToObject(config.getConfigValue(), CloudStorageConfig.class);
            aliyunOSSConfig.setAccessKeyId(cloudStorageConfig.getAliyunAccessKeyId());
            aliyunOSSConfig.setAccessKeySecret(cloudStorageConfig.getAliyunAccessKeySecret());
            aliyunOSSConfig.setBucket(cloudStorageConfig.getAliyunBucketName());
            aliyunOSSConfig.setEndpoint(cloudStorageConfig.getAliyunEndPoint());
            aliyunOSSConfig.setCdnEnpoint(cloudStorageConfig.getAliyunDomain());

            String backFilePath = ExcelWriter.getInstance().writeExcelFile(filePathNodes, backFileName, ExcelImportDevice.getBackFileColumnNames(new ExcelImportDevice()),
                    records, aliyunOSSConfig);

            importData.setFilePath(backFilePath + "?tmp=" + new Random().nextLong());
            return importData;
        } catch (Exception e) {
            log.error(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
            throw new BusinessException(e);
        }
    }

    private void readRecord(String filePath) throws BusinessException {
        try {
            // 获取工作表
            Map<String, Integer> colIndexMap = new HashMap<String, Integer>();
            Sheet sheet = ExcelHelper.getSheet(filePath, ExcelImportDevice.getColumnNames(new ExcelImportDevice()), 0, colIndexMap);

            // 读取数据
            records = new ArrayList<ExcelImportDevice>();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {// 逐行处理 excel
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                ExcelImportDevice record = new ExcelImportDevice();
                ExcelImportDevice.buildExcelBean(record, row, colIndexMap);
                if (!record.isEmpty()) {
                    records.add(record);
                }
            }

            if (records.isEmpty()) {
                throw new BusinessException("导入文件内容不能为空");
            }
        } catch (Exception e) {
            throw new BusinessException(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
    }

    private ImportData validateImportRecords() {
        // 验证字段
        return validateField();
    }

    private ImportData validateField() {
        ImportData importData = new ImportData();

        HashSet<String> phones = new HashSet<>();
        for (ExcelImportDevice record : records) {
            if (record.isFailed()) {
                continue;
            }
            // 验证imei
            if (!StringUtil.isNullOrBlank(record.getDeviceName())) {
                String imei = record.getDeviceName();
                if (imei.length() > 32) {
                    record.failed(ExcelImportDevice.COL_DEVICE_NAME, "不能超过32位");
                }
                if (imei.length() < 4) {
                    record.failed(ExcelImportDevice.COL_DEVICE_NAME, "不能低于4位");
                }
                if (phones.contains(record.getDeviceName())) {
                    record.failed(ExcelImportDevice.COL_DEVICE_NAME, "imei重复");
                } else {
                    phones.add(record.getDeviceName());
                }
            }
        }
        return importData;
    }

    private void doImport(String tenantId, String productKey) {

        for (ExcelImportDevice record : records) {
            if (record.isSuccess()) {
                try {
                    EquipmentCreateRq createRq = new EquipmentCreateRq();
                    createRq.setProductKey(productKey);
                    createRq.setNickname(record.getNickname());
                    createRq.setDeviceName(record.getDeviceName());
                    aliEquipmentService.create(tenantId, createRq, "表格导入");
                }catch (Exception e) {
                    record.setImportResult(ExcelBean.IMPRESULT_FAILED);
                    record.failed(ExcelImportDevice.COL_DEVICE_NAME, e.getMessage());
                }
            }
        }

    }
}
