/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * <p>
 * 项目名： commons-excel
 * 文件名： ExcelWriter.java
 * 模块说明：
 * 修改历史：
 * 2018年8月13日 - subinzhu - 创建。
 */
package com.jike.wlw.core.support.excel;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.DateUtil;
import com.geeker123.rumba.commons.util.StringUtil;
import com.jike.wlw.service.support.oss.AliyunOSSConfig;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel写入器
 *
 * @author subinzhu
 */
public class ExcelWriter {

    private static ExcelWriter instance = null;

    public static ExcelWriter getInstance() {
        if (instance == null) {
            synchronized (ExcelWriter.class) {
                if (instance == null) {
                    instance = new ExcelWriter();
                }
            }
        }
        return instance;
    }

    private Workbook writeWorkbook = null;
    private CellStyle dateStyle = null;
    private CellStyle colorDateStyle = null;
    private CellStyle colorStyle = null;

    /**
     * 写excel文件
     *
     * @param filePathNodes   路径结点（文件名），不包含斜杠
     * @param fileName        文件名，包括后缀
     * @param columnNames     excel文件列名，即列名
     * @param records         excel数据列表
     * @param aliyunOSSConfig 阿里OSS配置
     * @return
     */
    public String writeExcelFile(List<String> filePathNodes, String fileName, List<String> columnNames, List<? extends ExcelBean> records,
                                 AliyunOSSConfig aliyunOSSConfig) throws BusinessException {
        try {
            if (StringUtil.isNullOrBlank(fileName)) {
                throw new BusinessException("excel文件名不能为空");
            }
            if (columnNames == null || columnNames.isEmpty()) {
                throw new BusinessException("excel文件列名不能为空");
            }
            if (records == null) {
                throw new BusinessException("excel文件数据不能为空");
            }
            if (aliyunOSSConfig == null) {
                throw new BusinessException("阿里云OSS配置不能为空");
            }

            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equals("xls")) {
                writeWorkbook = new HSSFWorkbook();
                HSSFDataFormat fmt = (HSSFDataFormat) writeWorkbook.createDataFormat();
                dateStyle = writeWorkbook.createCellStyle();
                dateStyle.setDataFormat(fmt.getFormat("yyyy-M-dd HH:mm:ss"));

                colorDateStyle = writeWorkbook.createCellStyle();
                colorDateStyle.setDataFormat(fmt.getFormat("yyyy-M-dd HH:mm:ss"));
            } else if (suffix.equals("xlsx")) {
                writeWorkbook = new XSSFWorkbook();
                XSSFDataFormat fmt = (XSSFDataFormat) writeWorkbook.createDataFormat();
                dateStyle = writeWorkbook.createCellStyle();
                dateStyle.setDataFormat(fmt.getFormat("yyyy-M-dd HH:mm:ss"));

                colorDateStyle = writeWorkbook.createCellStyle();
                colorDateStyle.setDataFormat(fmt.getFormat("yyyy-M-dd HH:mm:ss"));
            } else {
                throw new RuntimeException("上传的文件不是xls或xlsx格式！");
            }
            colorDateStyle.setFillForegroundColor(IndexedColors.LIME.getIndex()); // 前景色
            colorDateStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

            colorStyle = writeWorkbook.createCellStyle();
            colorStyle.setFillForegroundColor(IndexedColors.LIME.getIndex()); // 前景色
            colorStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

            // 写临时文件
            String tempFilePath = writeTempFile(filePathNodes, fileName, columnNames, records);
            // 将临时文件转到七牛云
            File tempFile = new File(tempFilePath);
            if (!tempFile.exists()) {
                throw new RuntimeException("回写文件失败！");
            }

            //TODO 这个地方直接把oss路径传上来就行了
            String backFilePath = uploadExcelFileToAliOss(filePathNodes, fileName, new FileInputStream(tempFile), aliyunOSSConfig);

            // 删除临时文件
            tempFile.delete();

            return backFilePath;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 写入临时文件
     */
    private String writeTempFile(List<String> filePathNodes, String fileName, List<String> header, List<? extends ExcelBean> records) {
        // 获取项目路径
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取项目路径失败！", e);
        }
        if (!path.exists()) {
            path = new File("");
        }

        // 如果本地临时文件夹不存在，则新建。
        String tempFilePath = "/TEMP/";
        File tempFileDir = new File(path.getAbsolutePath() + tempFilePath);
        if (!tempFileDir.exists()) {
            tempFileDir.mkdir();
        }

        // 写 excel 文件
        Sheet sheet1 = writeWorkbook.createSheet();// 创建 sheet 对象
        Row headerRow = sheet1.createRow(0);// 第一行，标题
        for (int i = 0; i < header.size(); i++) {
            headerRow.createCell(i).setCellValue(header.get(i));
        }
        for (int i = 0; i < records.size(); i++) {// 循环创建数据行
            Row row = sheet1.createRow(i + 1);
            Map<String, Object> valuesMap = records.get(i).toValuesMap();
            boolean setForegroundColor = false;
            if (valuesMap.containsValue("合计")) {
                setForegroundColor = true;
            }
            for (int j = 0; j < header.size(); j++) {
                Object value = valuesMap.get(header.get(j));
                Cell cell = row.createCell(j);
                if (setForegroundColor) {
                    cell.setCellStyle(colorStyle);
                }
                if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Calendar) {
                    cell.setCellValue((Calendar) value);
                } else if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                    if (setForegroundColor) {
                        cell.setCellStyle(colorDateStyle);
                    } else {
                        row.getCell(j).setCellStyle(dateStyle);
                    }
                } else if (value instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) value).doubleValue());
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Float) {
                    cell.setCellValue((Float) value);
                } else if (value instanceof RichTextString) {
                    cell.setCellValue((RichTextString) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else {
                    cell.setCellValue((String) value);
                }
            }
        }
        FileOutputStream fos = null;
        try {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            tempFilePath += UUID.randomUUID().toString() + "." + suffix;
            fos = new FileOutputStream(path.getAbsolutePath() + tempFilePath);
            writeWorkbook.write(fos);// 写文件到本地
            return path.getAbsolutePath() + tempFilePath;
        } catch (Exception e) {
            throw new RuntimeException("写入excel文件失败！", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("关闭excel文件流失败！", e);
            }
        }
    }

    /**
     * 根据路径节点（文件名）和文件名构建完整的文件路径
     */
    private String buildFilePath(List<String> filePathNodes, String fileName) {
        String filePath = "";
        for (String pathNode : filePathNodes) {
            if (StringUtil.isNullOrBlank(pathNode)) {
                continue;
            }
            filePath += pathNode + "/";
        }
        filePath += fileName;
        return filePath;
    }

    /**
     * 将文件上传到七牛云
     */
    public String uploadExcelFileToAliOss(List<String> filePathNodes, String fileName, InputStream inputStream, AliyunOSSConfig aliyunOSSConfig) throws BusinessException {
        try {
            filePathNodes.add(0, "EXCEL");
            filePathNodes.add(2, DateUtil.formatToDay(new Date()));

            OSS client = (new OSSClientBuilder()).build(aliyunOSSConfig.getEndpoint(), aliyunOSSConfig.getAccessKeyId(), aliyunOSSConfig.getAccessKeySecret());

            String key = buildFilePath(filePathNodes, fileName);
            client.putObject(aliyunOSSConfig.getBucket(), key, inputStream);

            System.out.println("测试：" + aliyunOSSConfig.getCdnEnpoint() + "/" + key);
            return aliyunOSSConfig.getCdnEnpoint() + "/" + key;// oss-cn-shanghai.aliyuncs.com
        } catch (Exception e) {
            throw new BusinessException("上传excel文件到阿里云OSS失败：" + e.getMessage(), e);
        }
    }

}
