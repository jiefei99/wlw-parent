package com.jike.wlw.common;

import com.geeker123.rumba.commons.base.BaseConstants;
import com.geeker123.rumba.commons.util.StringUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * 常量获取工具类
 *
 * @author subinzhu
 */
public abstract class ConstantsUtils {

    public static final String ENV_DEV = "dev";
    public static final String ENV_PRD = "prd";
    public static final String ENV_TEST = "test";
    public static final String ENV_UAT = "uat";

    public static final List<String> envList = Arrays.asList(ENV_DEV, ENV_PRD, ENV_TEST, ENV_UAT);

    /**
     * 根据环境获取redisKey
     *
     * @param env 当前环境
     * @return
     */
    public static String getRedisKey(String env) {
        if (StringUtil.isNullOrBlank(env)) {
            return Constants.Redis.ROOT_PATH + ENV_DEV + Constants.Redis.KEY_WXA_TOKEN_FORMATTER;
        }
        if (!envList.contains(env)) {
            return Constants.Redis.ROOT_PATH + ENV_DEV + Constants.Redis.KEY_WXA_TOKEN_FORMATTER;
        }

        return Constants.Redis.ROOT_PATH + env + Constants.Redis.KEY_WXA_TOKEN_FORMATTER;
    }

}
