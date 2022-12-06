/**
 *
 */
package com.jike.wlw.config.fegin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ShadowX
 *
 */
@Data
public class ExceptionInfo implements Serializable {
    private static final long serialVersionUID = 4507668847597747141L;

    private String timestamp;

    private Integer status;

    private String exception;

    private String message;

    private String path;

    private String error;
}