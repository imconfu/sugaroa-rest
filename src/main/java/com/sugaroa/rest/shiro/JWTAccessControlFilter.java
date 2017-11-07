package com.sugaroa.rest.shiro;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAccessControlFilter extends AccessControlFilter {
    /**
     * 获取http header中的Authorization: Bearer (TOKEN)生成StatelessToken进行伪登录
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        System.out.println("JWTAccessControlFilter.isAccessAllowed");
        boolean accessAllowed = false;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorization = httpRequest.getHeader("Authorization");
        System.out.println("Authorization:" + authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return accessAllowed;
        }

        String bearer = authorization.substring(authorization.indexOf(" ") + 1);
        System.out.println("bearer:" + bearer);
        try {
            StatelessToken token = new StatelessToken(bearer);
            getSubject(request, response).login(token); //这里是关键！！！
            accessAllowed = true;
        } catch (SignatureVerificationException exception) {
            System.out.println("JWTVerificationException " + exception.toString());
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessAllowed;
    }

    /**
     * 访问禁止时，返回http forbidden
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("JWTAccessControlFilter.onAccessDenied");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.getWriter().write("access denied");
        return false;
    }
}
