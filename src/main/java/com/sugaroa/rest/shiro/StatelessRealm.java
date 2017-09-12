package com.sugaroa.rest.shiro;

import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.exception.AppException;
import com.sugaroa.rest.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.annotation.Resource;

public class StatelessRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    /**
     * 授权信息
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权信息:doGetAuthorizationInfo(PrincipalCollection principals)");
        return null;
    }

    /**
     * 身份验证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份验证:doGetAuthenticationInfo(AuthenticationToken token)");
        //UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        String username = (String) token.getPrincipal(); //用户名
        //char[] password = usernamePasswordToken.getPassword(); //密码

        System.out.println(username);
        System.out.println(token.getCredentials());

        User user = userService.findByUsername(username);
        if(user == null){
            throw new AppException("用户不存在！");
        }

        // 这里运算时实际为md5((username+salt)+password)，而不是md5(password+(username+salt))
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes((user.getCredentialsSalt())),
                getName());
    }
}
