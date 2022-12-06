package com.jike.wlw.common;

import com.geeker123.rumba.commons.base.BaseConstants;
import com.geeker123.rumba.commons.util.StringUtil;

/**
 * 系统常量
 *
 * @author subinzhu
 */
public  class Constants extends BaseConstants {

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
        String DEVICE_CODE = "DEVICE_CODE";
    }

    /**
     * 正则表达式
     */
    public interface Regex extends BaseConstants.Regex {
    }

    /**
     * Redis KEY
     */
    public interface Redis {
        /**
         * 根部
         */
        String ROOT_PATH = "wlw:";
        /**
         * 微信小程序登录token
         */
        String KEY_WXA_TOKEN_FORMATTER = ROOT_PATH + "wxaToken:user:%s:%s";
    }

    /**
     * 系统配置
     */
    public interface Config {
        /**
         * Config根部
         */
        String ROOT_CONFIG_PATH = "wlw.";

        /**
         * 分组：微信授权登录配置组
         */
        public static final String GROUP_LOGIN_JS_WLW = ROOT_CONFIG_PATH + "login.js";
        public static final String KEY_LOGIN_JS_APPID_WLW = ROOT_CONFIG_PATH + "login.js.appId";
        public static final String KEY_LOGIN_JS_SECRET_WLW = ROOT_CONFIG_PATH + "login.js.secret";

        /**
         * 分组：阿里云OSS对象存储
         */
        public static final String GROUP_OSS_WLW = ROOT_CONFIG_PATH + "oss";
        public static final String KEY_OSS_WLW_CONFIG_KEY = ROOT_CONFIG_PATH + "oss.config.key";

    }

    /**
     * 导出类型
     */
    public interface ExportType {
        public static final String WORK = "任务";
        public static final String WORK_LINE = "任务路线";
        public static final String ATTENDANCE = "考勤";
    }

}
