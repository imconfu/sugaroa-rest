package com.sugaroa.rest.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.exception.AppException;
import com.sugaroa.rest.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

public class StatelessRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {

        return token instanceof StatelessToken;
        //return super.supports(token);
    }

    /**
     * 授权信息
     * 从数据库读取授权信息(角色及权限)后保存在AuthorizationInfo里
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权信息:doGetAuthorizationInfo(PrincipalCollection principals)");
        String account = (String) principals.getPrimaryPrincipal();
        System.out.println("授权信息获取用户名:"+account);
        User currUser = userService.findByUsername(account);
        //获取用户role及permission信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("market");
//        authorizationInfo.addRole("role2");
//        authorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
//        authorizationInfo.addObjectPermission(new WildcardPermission("user1:*"));
//        authorizationInfo.addStringPermission("+user2+10");
//        authorizationInfo.addStringPermission("user2:*");
        return authorizationInfo;
    }

    /**
     * 身份验证
     * 一般是通过AuthenticationToken获取用户名，然后根据用户名获取密码，用密码等初始化SimpleAuthenticationInfo，然后对比两者进行检验
     * 在statelessRealm.setCredentialsMatcher中进行校验
     * 实质是难jwt token的合法性，在这里获取用户对应的secret12用来验证合法性
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份验证:doGetAuthenticationInfo(AuthenticationToken token)");

        String bearer = (String) token.getCredentials(); //用户名
        String account = (String) token.getPrincipal();
        return new SimpleAuthenticationInfo(account, bearer, getName());
//        try {
//            Algorithm algorithm = Algorithm.HMAC256("secret12");
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer("auth0")
//                    .build(); //Reusable verifier instance
//            DecodedJWT jwt = verifier.verify(bearer);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        //返回SimpleAuthenticationInfo给JWTCredentialsMatcher使用，进行验证
//        //UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
//
//        String username = (String) token.getPrincipal(); //用户名
//        //char[] password = usernamePasswordToken.getPassword(); //密码
//
//        System.out.println(username);
//        System.out.println(token.getCredentials());
//
//        User user = userService.findByUsername(username);
//        if (user == null) {
//            throw new AppException("用户不存在！");
//        }
//        System.out.println(user.getCredentialsSalt());
//
//        // 这里运算时实际为md5((username+salt)+password)，而不是md5(password+(username+salt))
//        return new SimpleAuthenticationInfo(
//                user,
//                user.getPassword(),
//                ByteSource.Util.bytes((user.getCredentialsSalt())),
//                getName());
    }
}
