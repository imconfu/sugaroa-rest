package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import java.io.UnsupportedEncodingException;

/**
 * 自定义CredentialsMatcher，验证jwt token的合法性
 * 个人理解：为了适应shiro的框架而这么实现
 */
public class JWTCredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        StatelessToken statelessToken = (StatelessToken) token;
        String bearer = (String) info.getCredentials();
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret12");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            verifier.verify(bearer);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
