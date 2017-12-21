package com.sugaroa.rest.activiti;

import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitiConfiguration extends AbstractProcessEngineAutoConfiguration {
//    @Bean(name = "processEngineConfiguration")
//    SpringProcessEngineConfiguration processEngineConfiguration(){
//        SpringProcessEngineConfiguration spec = new SpringProcessEngineConfiguration();
//        spec.createProcessEngineConfigurationFromResourceDefault();
//        System.out.println("ActivitiConfiguration.processEngineConfiguration run");
//        return spec;
//    }

}
