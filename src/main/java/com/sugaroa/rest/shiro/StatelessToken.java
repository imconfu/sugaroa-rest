package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {

    private String account;
    private String jsonWebToken;

    public StatelessToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    public Object getPrincipal() {
        if (account == null || account.isEmpty()) {
            DecodedJWT jwt = JWT.decode(jsonWebToken);
            account = jwt.getClaim("account").asString();
        }
        return account;
    }

    @Override
    public Object getCredentials() {
        return jsonWebToken;
    }
}
