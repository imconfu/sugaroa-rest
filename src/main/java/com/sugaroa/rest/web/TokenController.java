package com.sugaroa.rest.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.exception.AppException;
import com.sugaroa.rest.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
//@EnableAutoConfiguration
public class TokenController {
    private UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 授权token，验证用户名及密码
     *
     * @param account  用户名
     * @param password 密码
     * @return
     * @throws UnsupportedEncodingException
     */
    //(required=true, defaultValue="")
    @RequestMapping("/token/generate")
    Map<String, Object> generate(@NotBlank(message = "帐号不能为空") @RequestParam String account, @RequestParam String password) throws UnsupportedEncodingException {
        //userRepository.testAA();
        User user = userService.findByUsername(account);
        if (user == null) throw new AppException("用户不存在");

        //验证密码
        // 这里运算时实际为md5((username+salt)+password)
        String md5String = account + user.getSalt() + password;
        System.out.println(md5String);
        String md5Password = DigestUtils.md5DigestAsHex(md5String.getBytes("utf-8"));
        System.out.println(md5Password);
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
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("token", token);
        //result.put("privileges", user.getPrivilegeObject());
        return result;
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
    String refresh(@RequestParam(value = "token") String oldToken) {
        //String oldToken
        final String newToken = "new token";
        return newToken;
    }

    @RequestMapping("/")
    String home() {
        return "Hello Wor1ld!";
    }

    //@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    //User user(@PathVariable String id) {
    //    return userRepository.findByAccount(id);
    //}

//    @RequestMapping("/users")
//    Page<User> users() {
//        int page = 0, size = 10;
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        Pageable pageable = new PageRequest(page, size, sort);
//        return userRepository.findAll(pageable);
//    }

    @RequestMapping("/hello/{Name}")
    public String helloName(@PathVariable String Name) {
        return "Hello " + Name;
    }

}