//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jike.wlw.sys.web.filter;

import com.geeker123.rumba.commons.net.Url;
import com.geeker123.rumba.commons.util.StringUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.geeker123.rumba.sso.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class WebTokenFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String EXCLUDES = "excludes";
    public static final String LOGOUT_REQUEST = "_logout";
    private List<Pattern> excludeChecker;

    public WebTokenFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.logger.info("加载忽略的URL配置...");
        String excludeStr = filterConfig.getInitParameter("excludes");
        if (excludeStr != null) {
            String[] excludes = excludeStr.split(",");
            if (excludes != null) {
                String[] var4 = excludes;
                int var5 = excludes.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String exclude = var4[var6];
                    if (this.excludeChecker == null) {
                        this.excludeChecker = new ArrayList();
                    }

                    String regex = "^" + exclude.replaceAll("\\*", ".*").replaceAll("\\?", ".") + "$";
                    this.excludeChecker.add(Pattern.compile(regex));
                }
            }
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        if (this.isRequestUrlExcluded(httpRequest)) {
            this.logger.debug("url {} need not authenticate, ignored", httpRequest.getRequestURI());
            chain.doFilter(request, response);
        } else {
            ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(httpRequest.getServletContext());
            TokenService tokenService = appCtx.getBean(TokenService.class);
            TokenCallback callback = appCtx.getBean(TokenCallback.class);
            LoginConfig loginConfig = appCtx.getBean(LoginConfig.class);
            TokenContext.remove();
            if (httpRequest.getParameter("_logout") != null) {
                this.logger.debug("注销");
                tokenService.removeCookie(httpRequest, httpResponse);
                this.tokenRejected(httpRequest, httpResponse, loginConfig, callback);
            } else {
                String token = tokenService.extractTokenFromCookie(httpRequest);
                if (token == null) {
                    this.logger.debug("cookie中没有token");
                    this.tokenRejected(httpRequest, httpResponse, loginConfig, callback);
                } else {
                    this.logger.debug("token: {}", token);
                    TokenData tokenData = tokenService.verifyToken(token);
                    if (tokenData == null) {
                        this.logger.debug("token验证不通过: {}", token);
                        this.tokenRejected(httpRequest, httpResponse, loginConfig, callback);
                    } else {
                        this.logger.debug("用户信息: {}", tokenData);
                        String newToken = tokenService.refreshToken(httpRequest, httpResponse, token, tokenData, callback);
                        if (newToken == null) {
                            this.logger.debug("token身份无效: {}", tokenData);
                            this.tokenRejected(httpRequest, httpResponse, loginConfig, callback);
                        } else {
                            if (!newToken.equals(token)) {
                                this.logger.info("刷新token: {}", newToken);
                                tokenService.createCookie(httpRequest, httpResponse, newToken);
                            }

                            this.tokenAccepted(httpRequest, httpResponse, callback, tokenData);
                            chain.doFilter(request, response);
                        }
                    }
                }
            }
        }
    }

    private void tokenAccepted(HttpServletRequest req, HttpServletResponse resp, TokenCallback callback, TokenData tokenData) {
        if (callback != null) {
            callback.onAccepted(req, resp, tokenData);
        }

    }

    private void tokenRejected(HttpServletRequest req, HttpServletResponse resp, LoginConfig loginConfig, TokenCallback callback) throws IOException {
        if (callback != null) {
            callback.onRejected(req, resp);
        }

        if (!this.isAjaxRequest(req) && !StringUtil.isNullOrBlank(loginConfig.getLoginUrl())) {
            this.redirectToLogin(req, resp, loginConfig);
        } else {
            resp.sendError(401, "账号过期，请重新登录");
        }

    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp, LoginConfig loginConfig) throws IOException {
        String urlToRedirectTo = this.getQueryString(req);
        Url url = new Url(urlToRedirectTo);
        if (url.getQuery().contains("_logout")) {
            url.getQuery().remove("_logout");
            urlToRedirectTo = url.toString();
        }

        resp.sendRedirect(resp.encodeRedirectURL(loginConfig.getLoginUrl() + "?redirectUrl=" + URLEncoder.encode(urlToRedirectTo, "UTF-8")));
    }

    public void destroy() {
    }

    private String getQueryString(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer(request.getRequestURL());
        String query = request.getQueryString();
        if (query != null && query.length() > 0) {
            sb.append("?").append(query);
        }

        return sb.toString();
    }

    private boolean isRequestUrlExcluded(HttpServletRequest request) {
        if (this.excludeChecker == null) {
            return false;
        } else {
            StringBuffer urlBuffer = request.getRequestURL();
            if (request.getQueryString() != null) {
                urlBuffer.append("?").append(request.getQueryString());
            }

            String requestUri = urlBuffer.toString();
            Iterator var4 = this.excludeChecker.iterator();

            Pattern p;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                p = (Pattern)var4.next();
            } while(!p.matcher(requestUri).find());

            return true;
        }
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        return "XMLHttpRequest".equals(req.getHeader("x-requested-with"));
    }
}
