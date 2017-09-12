package com.sugaroa.rest.shiro;

import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatelessConfiguration {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean sffb  = new ShiroFilterFactoryBean();

        sffb.setSecurityManager(securityManager);

        return sffb;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(statelessRealm());
        //Subject工厂
        securityManager.setSubjectFactory(new StatelessSubjectFactory());
        //会话管理器
        securityManager.setSessionManager(sessionManager());

        //XML: securityManager.subjectDAO.sessionStorageEvaluator.sessionStorageEnabled = false
        //TODO 不知道这样写对不对
//        DefaultSubjectDAO subjectDAO = (DefaultSubjectDAO) securityManager.getSubjectDAO();
//        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = (DefaultWebSessionStorageEvaluator) subjectDAO.getSessionStorageEvaluator();
//        sessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
//        securityManager.setSubjectDAO(subjectDAO);


        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        ((DefaultSubjectDAO)securityManager.getSubjectDAO()).setSessionStorageEvaluator(sessionStorageEvaluator);

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
}

