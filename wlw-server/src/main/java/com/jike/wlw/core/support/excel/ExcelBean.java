/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名： commons-excel
 * 文件名： ExcelBean.java
 * 模块说明：
 * 修改历史：
 * 2018年8月13日 - subinzhu - 创建。
 */
package com.jike.wlw.core.support.excel;

import com.geeker123.rumba.commons.util.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * excel对象基类
 *
 * @author subinzhu
 */
public abstract class ExcelBean implements Serializable {
    private static final long serialVersionUID = -4810984793945678887L;

    /**
     * 导入回写文件的列名: 导入结果
     */
    public static final String COL_导入结果 = "导入结果";
    /**
     * 导入回写文件的列名: 导入信息
     */
    public static final String COL_导入信息 = "导入信息";

    /**
     * 导入回写文件的回写值: 成功
     */
    public static final String IMPRESULT_SUCCESS = "成功";
    /**
     * 导入回写文件的回写值: 失败
     */
    public static final String IMPRESULT_FAILED = "失败";
    /**
     * 导入回写文件的回写值: 忽略
     */
    public static final String IMPRESULT_IGNORE = "忽略";

    /**
     * 必填/选填字段说明备注
     */
    public static final String REMARK_INFO = "黄色字段为必填项，其余字段为选填";

    private String result = IMPRESULT_SUCCESS;
    private Map<String, String> errorColumnMsgMap = new LinkedHashMap<String, String>();
    private Map<String, String> successColumnMsgMap = new LinkedHashMap<String, String>();
    /**
     * 忽略
     */
    public void ignore() {
        result = IMPRESULT_IGNORE;
    }

    /**
     * 忽略，处理结果置为忽略，并添加忽略原因
     *
     * @param columnName 列名，not null
     * @param ignoreMsg  消息，not null
     */
    public void ignore(String columnName, String ignoreMsg) {
        ignore();
        addErrorColumnMessage(columnName, ignoreMsg);
    }

    /**
     * 失败，处理结果置为失败，并添加失败原因
     *
     * @param columnName 列名，not null
     * @param errorMsg   消息，not null
     */
    public void failed(String columnName, String errorMsg) {
        result = IMPRESULT_FAILED;
        addErrorColumnMessage(columnName, errorMsg);
    }

    /**
     * 成功，处理结果置为成功=
     *
     *@param columnName 列名，not null
     * @param successMsg   消息，not null
     */
    public void succeed(String columnName,String successMsg) {
        result = IMPRESULT_SUCCESS;
        addSuccessColumnMessage(columnName,successMsg);
    }

    /**
     * 添加错误原因或忽略原因
     *
     *@param columnName 列名，not null
     * @param successMsg   消息，not null
     */
    private void addSuccessColumnMessage(String columnName,String successMsg) {
        if (StringUtil.isNullOrBlank(columnName) || StringUtil.isNullOrBlank(successMsg)) {
            return;
        }

        String msg = successColumnMsgMap.get(columnName);
        if (StringUtil.isNullOrBlank(msg)) {
            successColumnMsgMap.put(columnName, successMsg);
        } else {
            successColumnMsgMap.put(columnName, msg + "；" + successMsg);
        }

    }
    /**
     * 添加错误原因或忽略原因
     *
     * @param columnName 列名，not null
     * @param errorMsg   消息，not null
     */
    private void addErrorColumnMessage(String columnName, String errorMsg) {
        if (StringUtil.isNullOrBlank(columnName) || StringUtil.isNullOrBlank(errorMsg)) {
            return;
        }

        String msg = errorColumnMsgMap.get(columnName);
        if (StringUtil.isNullOrBlank(msg)) {
            errorColumnMsgMap.put(columnName, errorMsg);
        } else {
            errorColumnMsgMap.put(columnName, msg + "；" + errorMsg);
        }
    }

    public boolean isSuccess() {
        return IMPRESULT_SUCCESS.equals(result);
    }

    public boolean isIgnore() {
        return IMPRESULT_IGNORE.equals(result);
    }

    public boolean isFailed() {
        return IMPRESULT_FAILED.equals(result);
    }

    public String getImportMsgByColumnName(String columnName) {
        return errorColumnMsgMap.get(columnName);
    }

    public String getImportResult() {
        return result;
    }

    public void setImportResult(String result) {
        this.result = result;
    }

    public abstract boolean isEmpty();

    public abstract Map<String, Object> toValuesMap();

    public String getImportMsg() {
        String importMsg = "";
        if (errorColumnMsgMap == null || errorColumnMsgMap.isEmpty()) {
            return importMsg;
        }
        for (Entry<String, String> entry : errorColumnMsgMap.entrySet()) {
            importMsg += entry.getKey() + "：" + entry.getValue() + "；";
        }
        return importMsg;
    }

    /**
     * 获取文件列名列表
     */
    public static List<String> getColumnNames(Object obj) {
        try {
            List<String> columnNames = new ArrayList<>();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (fieldName.startsWith("COL_")) {
                    // 要设置属性可达，不然会抛出IllegalAccessException异常
                    field.setAccessible(true);
                    columnNames.add((String) field.get(obj));
                }
            }
            return columnNames;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("执行getColumnNames出错");
        }
    }

    /**
     * 获取回写文件列名列表
     */
    public static List<String> getBackFileColumnNames(Object obj) {
        List<String> backFileColumnNames = getColumnNames(obj);
        backFileColumnNames.add(COL_导入结果);
        backFileColumnNames.add(COL_导入信息);
        return backFileColumnNames;
    }

    /**
     * 构建excel对象
     */
    public static void buildExcelBean(Object obj, Row row, Map<String, Integer> colIndexMap) throws IllegalArgumentException, IllegalAccessException,
            NoSuchMethodException, SecurityException, InvocationTargetException {
        // 先将单元格格式转为String类型
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell == null) {
                continue;
            }
            if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
        }
        // 设置字段
        List<Field> valueFields = getValueFields(obj);
        List<String> columnNames = getColumnNames(obj);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.getCell(colIndexMap.get(columnNames.get(i)));
            if (cell == null) {
                continue;
            }
            Field field = valueFields.get(i);
            String fieldName = field.getName();
            String methodStr = "set" + fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1);
            Method method = obj.getClass().getMethod(methodStr, new Class[]{
                    field.getType()});
            if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC || !DateUtil.isCellDateFormatted(cell)) {
                method.invoke(obj, ExcelHelper.getStringCellValue(cell));
            } else {
                method.invoke(obj, com.geeker123.rumba.commons.util.DateUtil.formatToDateTime(cell.getDateCellValue()));
            }
        }
    }

    /**
     * 获取值字段
     */
    protected static List<Field> getValueFields(Object obj, String... ignoreFieldNames) {
        Set<String> ignoreFieldNamesSet = new HashSet<>();
        if (ignoreFieldNames != null && ignoreFieldNames.length > 0) {
            ignoreFieldNamesSet = new HashSet<>(Arrays.asList(ignoreFieldNames));
        }
        List<Field> valueFields = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.startsWith("COL_") && !"serialVersionUID".equals(fieldName) && !ignoreFieldNamesSet.contains(fieldName)) {
                valueFields.add(field);
            }
        }
        return valueFields;
    }

    /**
     * 判断是否为空
     */
    protected static boolean isEmpty(Object obj, String... ignoreFieldNames) {
        try {
            boolean isNotEmpty = false;
            List<Field> valueFields = getValueFields(obj);
            for (int i = 0; i < valueFields.size(); i++) {
                Field valueField = valueFields.get(i);
                // 要设置属性可达，不然会抛出IllegalAccessException异常
                valueField.setAccessible(true);
                Object value = valueField.get(obj);
                if (value != null) {
                    if (value instanceof String) {
                        if (!StringUtil.isNullOrBlank((String) value)) {
                            isNotEmpty = true;
                            break;
                        }
                    } else {
                        isNotEmpty = true;
                        break;
                    }
                }
            }
            return !isNotEmpty;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("执行isEmpty出错");
        }
    }

    /**
     * 转换成导出数据Map
     */
    protected Map<String, Object> toValuesMap(Object obj) {
        try {
            Map<String, Object> valuesMap = new HashMap<String, Object>();
            List<String> columnNames = getColumnNames(obj);
            List<Field> valueFields = getValueFields(obj);
            for (int i = 0; i < columnNames.size(); i++) {
                Field valueField = valueFields.get(i);
                // 要设置属性可达，不然会抛出IllegalAccessException异常
                valueField.setAccessible(true);
                valuesMap.put(columnNames.get(i), valueField.get(obj));
            }
            valuesMap.put(COL_导入结果, getImportResult());
            valuesMap.put(COL_导入信息, getImportMsg());
            return valuesMap;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("执行toValuesMap出错");
        }
    }
}
