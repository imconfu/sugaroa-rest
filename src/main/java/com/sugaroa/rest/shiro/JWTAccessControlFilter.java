package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class JWTAccessControlFilter extends AccessControlFilter {
//    @Autowired 不生效
//    private UserService userService;

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
            //DecodedJWT jwtTest = JWT.decode(bearer);
            //String username = jwtTest.getClaim("account").asString();
            StatelessToken token = new StatelessToken(bearer);
            getSubject(request, response).login(token);
            accessAllowed = true;

//            Algorithm algorithm = Algorithm.HMAC256("secret12");
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer("auth0")
//                    .build(); //Reusable verifier instance
//            DecodedJWT jwt = verifier.verify(bearer);
//
//            accessAllowed = true;
//
//            // TODO 获取用户帐号或ID做一些操作
//            //userService.get(jwt.getClaim("id").asInt());
//            // 类似于Session,把用户Id和名字存起来
//            //String subjectName = (String) SecurityUtils.getSubject().getPrincipal();
//
//            //在这里将用户名信息这些放在Subject里
//            //Subject currentUser = SecurityUtils.getSubject();
//            //currentUser.login();    //TODO 要使用StatelessToken
//            request.setAttribute("userId", jwt.getClaim("id").asInt());
//            request.setAttribute("userAccount", jwt.getClaim("account").asString());
        } catch (SignatureVerificationException exception) {
            System.out.println("JWTVerificationException " + exception.toString());
            throw exception;
        }
//        catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return accessAllowed;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("JWTAccessControlFilter.onAccessDenied");
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpResponse.getWriter().write("access denied");
        return false;
    }
}
