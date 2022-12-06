/**
 * 
 */
package com.jike.wlw.common;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ShadowX
 *
 */
@Getter
@Setter
@ApiModel("ID名称值")
public class IdNameValue implements Serializable {
  private static final long serialVersionUID = -4958395377464005696L;

  private String id;
  private String name;
  private String value;

}
