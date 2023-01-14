/**
 * 
 */
package com.jike.wlw.sys.web.config.fegin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ShadowX
 *
 */
@Data
public class ExceptionInfo implements Serializable {
  private static final long serialVersionUID = -924364614966991305L;


  private String timestamp;

  private Integer status;

  private String exception;

  private String message;

  private String path;

  private String error;
}