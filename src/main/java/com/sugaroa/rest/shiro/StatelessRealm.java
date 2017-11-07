package com.sugaroa.rest.shiro;

import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class StatelessRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {

        return token instanceof StatelessToken;
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
        System.out.println("授权信息获取用户名:" + account);
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
     * 实质是验证jwt token的合法性，在这里获取用户对应的secret12用来验证合法性
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份验证:doGetAuthenticationInfo(AuthenticationToken token)");

        //这里先不验证用户是否存在或禁用
        String bearer = (String) token.getCredentials(); //用户名
        String account = (String) token.getPrincipal();
        return new SimpleAuthenticationInfo(account, bearer, getName());
    }
}
