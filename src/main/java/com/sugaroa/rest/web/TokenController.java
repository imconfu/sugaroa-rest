package com.sugaroa.rest.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.exception.AppException;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
//@EnableAutoConfiguration
public class TokenController {
    private UserRepository userRepository;

    public TokenController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 授权token，验证用户名及密码
     *
     * @param account  用户名
     * @param password 密码
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/token/grant")
    //(required=true, defaultValue="")
    Result grant(@RequestParam String account, @RequestParam String password) throws UnsupportedEncodingException {

        User user = userRepository.findByAccount(account);
        if (user == null) {
            throw new AppException("用户不存在");
        }

        Algorithm algorithm = Algorithm.HMAC256("secret12");
        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("account", "confu")
                .sign(algorithm);
        return new Result(1, token);
    }

    @RequestMapping("/token/verify")
    String verify(@RequestHeader("Token") String Token) {
        //        JSONObject result = new JSONObject();
        //result.put("token", token);

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
    String refresh(@RequestParam(value = "token") String oldToken) {
        //String oldToken
        final String newToken = "new token";
        return newToken;
    }

    @RequestMapping("/")
    String home() {
        return "Hello Wor1ld!";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    User user(@PathVariable String id) {
        return userRepository.findByAccount(id);
    }

    @RequestMapping("/users")
    Page<User> users() {
        int page = 0, size = 10;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @RequestMapping("/hello/{Name}")
    public String helloName(@PathVariable String Name) {
        return "Hello " + Name;
    }

}