package com.jike.wlw.sys.web;

import com.geeker123.rumba.commons.base.BaseConstants;

/**
 * 系统常量
 *
 * @author subinzhu
 */
public class Constants extends BaseConstants {

    /**
     * 常用操作
     */
    public interface Action extends BaseConstants.Action {

    }

    /**
     * 错误代码和消息常量。 1~99以及200、500、999等为预留代码，请勿覆盖
     */
    public interface Error extends BaseConstants.Error {

    }

    /**
     * 字段常量
     */
    public interface Field extends BaseConstants.Field {

    }

    /**
     * 正则表达式
     */
    public interface Regex extends BaseConstants.Regex {

    }

    /**
     * 终端类型
     */
    public interface ClientType {
        /**
         * APP
         */
        public static final String APP = "APP";
        /**
         * PC
         */
        public static final String PC = "PC";
    }

    /**
     * 设备类型
     */
    public interface DeviceType {
        /**
         * 安卓
         */
        public static final String ANDROID = "ANDROID";
        /**
         * 苹果
         */
        public static final String IOS = "IOS";
        /**
         * PC
         */
        public static final String PC = "PC";
    }

    /**
     * Redis KEY
     */
    public interface Redis {
        /**
         * 根部
         */
        public static final String ROOT_PATH = "wlw:";
        /**
         * 登录token
         */
        public static final String KEY_TOKEN_FORMATTER = ROOT_PATH + "token:user:%s:%s";
    }
}
