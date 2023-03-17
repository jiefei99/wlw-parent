/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2019，所有权利保留。
 * 
 * 项目名： commons-excel
 * 文件名： ImportInfo.java
 * 模块说明：    
 * 修改历史：
 * 2018年8月13日 - subinzhu - 创建。
 */
package com.jike.wlw.core.support.excel;

import java.io.Serializable;

/**
 * 导入结果信息
 * 
 * @author subinzhu
 *
 */
public class ImportInfo implements Serializable {
  private static final long serialVersionUID = 2388747588919062647L;

  private long successCount = 0;
  private long failedCount = 0;
  private long ignoreCount = 0;
  private String backFile;

  public ImportInfo() {
    super();
  }

  public ImportInfo(long successCount, long failedCount, long ignoreCount, String backFile) {
    super();
    this.successCount = successCount;
    this.failedCount = failedCount;
    this.ignoreCount = ignoreCount;
    this.backFile = backFile;
  }

  /** 成功条数 */
  public long getSuccessCount() {
    return successCount;
  }

  public void setSuccessCount(long successCount) {
    this.successCount = successCount;
  }

  /** 失败条数 */
  public long getFailedCount() {
    return failedCount;
  }

  public void setFailedCount(long failedCount) {
    this.failedCount = failedCount;
  }

  /** 忽略条数 */
  public long getIgnoreCount() {
    return ignoreCount;
  }

  public void setIgnoreCount(long ignoreCount) {
    this.ignoreCount = ignoreCount;
  }

  /** 回写文件路径 */
  public String getBackFile() {
    return backFile;
  }

  public void setBackFile(String backfile) {
    this.backFile = backfile;
  }

}
