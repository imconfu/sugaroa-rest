package com.sugaroa.rest.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration Enter！");
        ShiroFilterFactoryBean sffb = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        sffb.setSecurityManager(securityManager);

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        // 特别注意：这里不能用jwtAccessControlFilter函数作为put的值
        // 否则所有的请求都会被jwt这个过滤器拦截，还不知道为什么！！
        filters.put("jwt", new JWTAccessControlFilter());
        //filters.put("jwtAF", jwtAuthenticationFilter());
        sffb.setFilters(filters);

        //设置登录地址，即：这个地址访问不验证身份及权限这些
        //sffb.setLoginUrl("/token/grant");

        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        //"/**"这个过滤器放在最下面，authc：必须认证通过才可以访问
        filterChainDefinitionMap.put("/token/grant", "anon");
        filterChainDefinitionMap.put("/token/generate", "anon");
        filterChainDefinitionMap.put("/**", "jwt");
        //filterChainDefinitionMap.put("/users/**", "jwtACF");
        //filterChainDefinitionMap.put("/token/grant", "anon");
        //filterChainDefinitionMap.put("/**", "test");
        //filterChainDefinitionMap.put("/user/**", "jwtACF");//noSessionCreation,JWTACF

        sffb.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return sffb;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(statelessRealm());

        //XML: securityManager.subjectDAO.sessionStorageEvaluator.sessionStorageEnabled = false
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);

        securityManager.setSubjectDAO(subjectDAO);

        //Subject工厂
        securityManager.setSubjectFactory(new StatelessSubjectFactory());

        //会话管理器
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * Realm实现
     * TODO 这里继承SimpleCredentialsMatcher用自定义Matcher验证token
     *
     * @return
     */
    @Bean
    public StatelessRealm statelessRealm() {
        StatelessRealm statelessRealm = new StatelessRealm();
        statelessRealm.setCachingEnabled(false);
        //statelessRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        //设置自定义的jwt验证
        statelessRealm.setCredentialsMatcher(jwtCredentialsMatcher());
        return statelessRealm;
    }

    /**
     * 会话管理器
     */
    @Bean
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);

        return sessionManager;
    }

    /**
     * 密码的处理方法，md5(salt+passwork) 1次
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    /**
     * 验证JWT的CredentialsMatcher
     *
     * @return
     */
    @Bean
    public JWTCredentialsMatcher jwtCredentialsMatcher() {
        JWTCredentialsMatcher jwtCredentialsMatcher = new JWTCredentialsMatcher();
        return jwtCredentialsMatcher;
    }

    @Bean
    public JWTAccessControlFilter jwtAccessControlFilter() {
        return new JWTAccessControlFilter();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}

