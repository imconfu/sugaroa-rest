package com.sugaroa.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.entity.User;
import com.sugaroa.rest.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
//@EnableAutoConfiguration
public class TokenController {
    private UserRepository userRepository;

    public TokenController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * 授权token，验证用户名及密码
     * @param account 用户名
     * @param password 密码
     * @return result json string
     * @throws JSONException
     */
    @RequestMapping("/token/grant")
    String grant(@RequestParam String account, @RequestParam(required=true, defaultValue="") String password) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("success", false);
        //String account, String password
        try {
            //根据帐号查找用户
            User user = userRepository.findByAccount(account);
            System.out.print(user);

            Algorithm algorithm = Algorithm.HMAC256("secret12");
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("account", "confu")
                    .sign(algorithm);
            result.put("token", token);
            result.put("success", true);
            result.put("user", user.toString());
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
            result.put("message", exception.toString());
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
            result.put("message", exception.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            result.put("message", e.toString());
        }
        return result.toString();
    }

    @RequestMapping("/token/verify")
    String verify(@RequestHeader("Token") String Token) {
        String result = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret12");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(Token);
            result = jwt.toString();
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
            result = exception.toString();
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            result = exception.toString();
        }

        DecodedJWT decode = JWT.decode(Token);

//        return "Bearer="+Token+",result="+result+",decode.getHeader="+decode.getHeader()
//                +",decode.getPayload="+decode.getPayload()
//                +",decode.getToken="+decode.getToken()
//                +",decode.getSignature="+decode.getSignature();
        return "Bearer=" + Token + ",result=" + result + ",decode.getClaims=" + decode.getClaim("account").asString();
    }

    /**
     * 刷新Token
     *
     * @param oldToken
     * @return
     */
    @RequestMapping("/token/refresh")
    String refresh(@RequestParam(value="token") String oldToken) {
        //String oldToken
        final String newToken = "new token";
        return newToken;
    }

    @RequestMapping("/")
    String home() {
        return "Hello Wor1ld!";
    }

    @RequestMapping("/hello/{Name}")
    public String helloName(@PathVariable String Name) {
        return "Hello " + Name;
    }
}