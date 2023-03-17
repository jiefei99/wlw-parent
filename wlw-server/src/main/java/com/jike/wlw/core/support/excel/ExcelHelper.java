/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名： commons-excel
 * 文件名： ExcelHelper.java
 * 模块说明：
 * 修改历史：
 * 2018年8月13日 - subinzhu - 创建。
 */
package com.jike.wlw.core.support.excel;

import com.geeker123.rumba.commons.util.DateUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.geeker123.rumba.commons.util.XSSStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author subinzhu
 */
public class ExcelHelper {

    /**
     * 导入回写文件后缀
     */
    public static final String FILE_SUFFIX = "_back";

    public static String generateBackFile(String filePath, String fileName) {
        int index = filePath.lastIndexOf("/");
        String file = filePath.substring(index + 1);

        index = file.lastIndexOf(".");
        String suffix = file.substring(index + 1);

        return fileName + FILE_SUFFIX + "." + suffix;
    }

    /**
     * 获取工作表（只获取第一个），并获取用于存放列名和列序号的Map
     *
     * @param filePath        文件路径，not null
     * @param columnNames     文件列名，not null
     * @param rowStartedIndex 列名起始行index，从0开始算，not null
     * @param colIndexMap     用于存放列名和列序号，not null
     * @return 工作表
     */
    public static Sheet getSheet(String filePath, List<String> columnNames, int rowStartedIndex, Map<String, Integer> colIndexMap) {
        try {
            if (filePath == null) {
                throw new RuntimeException("filePath不能为空");
            }
            if (columnNames == null || columnNames.isEmpty()) {
                throw new RuntimeException("columnNames不能为空");
            }
            if (colIndexMap == null) {
                throw new RuntimeException("colIndexMap不能为空");
            }

            filePath = filePath.replaceAll(" ", "%20");// 替换空格，否则会报错
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            Workbook readWorkbook = null;
            URL url = new URL(filePath);
            if (suffix.equals("xls")) {
                try {
                    readWorkbook = new HSSFWorkbook(new BufferedInputStream(url.openStream()));
                } catch (OfficeXmlFileException ex) {
                    try {
                        //兼容直接改excel文件格式后缀，xlsx->xls
                        readWorkbook = new XSSFWorkbook(new BufferedInputStream(url.openStream()));
                    } catch (OfficeXmlFileException e) {
                        throw new RuntimeException(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
                    }
                }
            } else if (suffix.equals("xlsx")) {
                try {
                    InputStream stream = url.openStream();
                    BufferedInputStream inputStream = new BufferedInputStream(stream);
                    readWorkbook = new XSSFWorkbook(inputStream);
                } catch (OfficeXmlFileException ex) {
                    try {
                        //兼容直接改excel文件格式后缀，xls->xlsx
                        readWorkbook = new HSSFWorkbook(new BufferedInputStream(url.openStream()));
                    } catch (OfficeXmlFileException e) {
                        throw new RuntimeException(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
                    }
                }
            } else {
                throw new RuntimeException("xls或xlsx");
            }
            // 读取第一个 sheet
            Sheet sheet = readWorkbook.getSheetAt(0);
            Row columnNameRow = sheet.getRow(rowStartedIndex);
            // 验证列名
            Map<String, String> colNamesMap = new HashMap<String, String>();
            for (Cell cell : columnNameRow) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String colName = cell.getStringCellValue();
                if (colName != null) {
                    colName = StringUtil.replaceBrackets(StringUtil.clean(colName.trim()));
                }
                colNamesMap.put(colName, colName);
            }
            StringBuffer errorMsg = new StringBuffer();
            errorMsg.append("缺失列：");
            for (int i = 0; i < columnNames.size(); i++) {
                if (colNamesMap.get(columnNames.get(i)) == null) {
                    errorMsg.append(columnNames.get(i) + ";");
                }
            }
            if (!errorMsg.toString().equals("缺失列：")) {
                errorMsg.append("<br><b style='color:red;'>请在导入窗口下载最新导入模板后重试！</b>");
                throw new RuntimeException(errorMsg.toString());
            }

            // 更新colIndexMap
            colIndexMap.clear();
            for (int i = 0; i < columnNameRow.getLastCellNum(); i++) {
                Cell cell = columnNameRow.getCell(i);
                String colName = cell.getStringCellValue();
                if (colName != null) {
                    colName = StringUtil.replaceBrackets(colName.trim());
                }
                colIndexMap.put(colName, i);
            }

            return sheet;
        } catch (Exception e) {
            throw new RuntimeException(StringUtil.isNullOrBlank(e.getMessage()) ? e.toString() : e.getMessage());
        }
    }

    /**
     * 读取日期
     *
     * @param cell
     * @return
     */
    public static Date getDateCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        cell.getCellStyle().getDataFormatString();
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getDateCellValue();
        } else {
            try {
                String dateStr = getStringCellValue(cell);
                if (ExcelBean.REMARK_INFO.equals(dateStr)) {
                    return null;
                }
                return DateUtil.convertToDate(dateStr);
            } catch (Exception e) {
                try {
                    String dateStr = getStringCellValue(cell);
                    if (ExcelBean.REMARK_INFO.equals(dateStr)) {
                        return null;
                    }

                    double value = new Double(dateStr);
                    CTWorkbookPr workbookPr = ((XSSFWorkbook) cell.getSheet().getWorkbook()).getCTWorkbook().getWorkbookPr();
                    boolean date1904 = workbookPr != null && workbookPr.getDate1904();
                    return org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value, date1904);
                } catch (Exception e1) {
                    throw new RuntimeException("不是日期格式");
                }
            }
        }
    }

    /**
     * 读取文本
     *
     * @param cell
     * @return
     */
    public static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        String value = StringUtil.clean(cell.toString());
        value = XSSStripper.stripXSS(value);
        return value;
    }

}
