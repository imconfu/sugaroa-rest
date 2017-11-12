package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {

    private Integer userID;
    private String jsonWebToken;

    public StatelessToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    public Object getPrincipal() {
        if (userID == null) {
            DecodedJWT jwt = JWT.decode(jsonWebToken);
            userID = jwt.getClaim("id").asInt();
        }
        return userID;
    }

    @Override
    public Object getCredentials() {
        return jsonWebToken;
    }
}
