/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月19日 14:35 - ASUS - 创建。
 */
package com.jike.wlw.core.equipment.imp;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.core.support.excel.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
@Slf4j
@Service
public class EquipmentNameImporter {
    private List<ExcelImportDeviceName> records = new ArrayList<>();

    public List<String> doImport(String tenantId, String filePath) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(filePath)) {
                throw new BusinessException("文件路径不能为空");
            }
            // 读取文件
//            String fileName = null;
//            fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            readRecord(filePath);
            // 验证导入信息
            List<String> nameList = validateField();
            return nameList;
        } catch (Exception e) {
            log.error(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
            throw new BusinessException(e);
        }
    }

    private void readRecord(String filePath) throws BusinessException {
        try {
            // 获取工作表
            Map<String, Integer> colIndexMap = new HashMap<String, Integer>();
            Sheet sheet = ExcelHelper.getSheet(filePath, ExcelImportDeviceName.getColumnNames(new ExcelImportDeviceName()), 0, colIndexMap);

            // 读取数据
            records = new ArrayList<ExcelImportDeviceName>();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {// 逐行处理 excel
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                ExcelImportDeviceName record = new ExcelImportDeviceName();
                ExcelImportDeviceName.buildExcelBean(record, row, colIndexMap);
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

    private List<String> validateField() {
        HashSet<String> nameList = new HashSet<>();
        for (ExcelImportDeviceName record : records) {
            if (record.isFailed()) {
                continue;
            }
            if (!StringUtil.isNullOrBlank(record.getDeviceName())) {
                //这里可能要校验文件名格式
                nameList.add(record.getDeviceName());
            }
        }
        return new ArrayList<>(nameList);
    }

}
