package com.sugaroa.rest.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oracle.tools.packager.Log;
import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

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
        userRepository.testAA();
        User user = userRepository.findByAccount(account);
        if (user == null) throw new AppException("用户不存在");

        //验证密码
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        String md5Password = passwordEncoder.encodePassword(password, user.getSalt());
        //System.out.println(md5Password);
        if (!md5Password.equals(user.getPassword())) {
            throw new AppException("密码错误");
        }

        // 生成token
        Algorithm algorithm = Algorithm.HMAC256("secret12");
        String token = JWT.create()
                .withIssuer("auth0")        //iss: jwt签发者
                .withSubject("user")        //sub: jwt所面向的用户
                //.withAudience()             //aud: 接收jwt的一方
                //.withExpiresAt()          //exp: jwt的过期时间，这个过期时间必须要大于签发时间
                //.withNotBefore()          //nbf: 定义在什么时间之前，该jwt都是不可用的.
                .withIssuedAt(new Date())   //iat: jwt的签发时间
                //.withJWTId()              //jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
                .withClaim("id", user.getId())
                .withClaim("account", account)
                .sign(algorithm);
        return new Result("token", token);
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