/**
 * 版权所有©，极客软创（厦门）信息科技有限公司，2018，所有权利保留。
 * <p>
 * 项目名： chillbaby-wechatweb
 * 文件名： FlowCodeGenerator.java
 * 模块说明：
 * 修改历史：
 * 2018年9月18日 - subinzhu - 创建。
 */
package com.jike.wlw.dao.flowcode;

import com.geeker123.rumba.jdbc.entity.JdbcEntity;
import com.geeker123.rumba.jdbc.entity.PEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author chenpeisi
 */
@Setter
@Getter
public class PFlowCode extends PEntity implements JdbcEntity {

  /**
   * 属性 {@link #getFlowName()}最大长度限制。
   */
  public static final int LENGTH_FLOWNAME = 64;
  public static final String TABLE_NAME = "bwi_flow_code";
  private static final long serialVersionUID = -1898157338935333289L;

  @ApiModelProperty("流水码序列名")
  private String flowName;
  @ApiModelProperty("流水码")
  private String code;
  @ApiModelProperty("生成时间 ")
  private Date created;

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }
}
