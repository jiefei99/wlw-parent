/**
 *
 */
package com.jike.wlw.sys.web.config.fegin;

import com.geeker123.rumba.commons.exception.BusinessException;
import com.geeker123.rumba.commons.util.JsonUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShadowX
 *
 */
@Configuration
@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String var1, Response response) {
        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader());
                ExceptionInfo exceptionInfo = JsonUtil.jsonToObject(body, ExceptionInfo.class);

                if (exceptionInfo.getException() != null) {
                    Class clazz = Class.forName(exceptionInfo.getException());
                    return (Exception) clazz.getDeclaredConstructor(String.class)
                            .newInstance(exceptionInfo.getMessage());
                } else {
                    return new BusinessException(exceptionInfo.getMessage());
                }
            }
        } catch (Exception var4) {
            log.error(var4.getMessage());
            return new BusinessException(var4.getMessage());
        }
        return new BusinessException("系统异常,请联系管理员");
    }

}
