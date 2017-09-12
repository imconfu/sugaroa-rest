package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAccessControlFilter extends AccessControlFilter {
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
            Algorithm algorithm = Algorithm.HMAC256("secret12");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(bearer);
            accessAllowed = true;

            // TODO 获取用户帐号或ID做一些操作
            String username = jwt.getClaim("account").asString();
            //String subjectName = (String) SecurityUtils.getSubject().getPrincipal();

        } catch (SignatureVerificationException exception) {
            System.out.println("JWTVerificationException " + exception.toString());
            throw exception;
        }

        return accessAllowed;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("JWTAccessControlFilter.onAccessDenied");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }
}
