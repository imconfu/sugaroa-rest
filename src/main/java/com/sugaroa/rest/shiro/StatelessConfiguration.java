package com.sugaroa.rest.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class StatelessConfiguration {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean sffb  = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        sffb.setSecurityManager(securityManager);

        Map<String, Filter> filters = new LinkedHashMap<String,Filter>();
        filters.put("jwtACF", jwtAccessControlFilter());
        sffb.setFilters(filters);

        //设置登录地址，即：这个地址访问不验证身份及权限这些
        //sffb.setLoginUrl("/token/grant");

        // 拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        //"/**"这个过滤器放在最下面，authc：必须认证通过才可以访问
        filterChainDefinitionMap.put("/token/grant", "anon");
        //filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/**", "noSessionCreation, jwtACF");//,JWTACF

        sffb.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return sffb;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
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
     * @return
     */
    @Bean
    public StatelessRealm statelessRealm(){
        StatelessRealm statelessRealm = new StatelessRealm();
        statelessRealm.setCachingEnabled(false);
        statelessRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return statelessRealm;
    }

    /**
     * 会话管理器
     */
    @Bean
    public DefaultSessionManager sessionManager(){
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);

        return sessionManager;
    }

    /**
     * 密码的处理方法，md5(passwork+salt) 1次
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    @Bean
    public JWTAccessControlFilter jwtAccessControlFilter(){
        return new JWTAccessControlFilter();
    }
}

