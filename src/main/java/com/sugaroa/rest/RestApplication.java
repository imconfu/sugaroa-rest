package com.sugaroa.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

    /**
     * 要控制器中使用@Validated用，必须在这里注入！！
     * 对于传入的request参数可校验，即：
     * 1、request有传参数，值不合法时抛出ConstraintViolationException，
     * 2、request没有传参数，有定义@RequestParam且required = true时，优先抛出MissingServletRequestParameterException
     * @return
     */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}
}
