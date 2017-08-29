package com.sugaroa.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ControllerAdvice 主要处理的就是 controller 层的错误信息，而没有进入 controller 层的错误 @ControllerAdvice 是无法处理的，
 * 那么我需要另外的一个全局错误处理，如Map<String, Object>Controller。
 */
@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {

    private Map<String, Object> result;

    public AppExceptionHandler() {
        result = new HashMap<String, Object>();
        result.put("code", 0);
        result.put("error", "System Exception");
        result.put("message", "错误信息");
        result.put("exception", "异常原因");
    }

    /**
     * 处理参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        this.result.put("message", "缺少参数：" + e.getParameterName() + "!");
        return new ResponseEntity<Map<String, Object>>(this.result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppException.class})
    @ResponseBody
    public Map<String, Object> AppExceptionHandler(AppException e) {
        this.result.put("message", e.getMessage());
        return this.result;
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public Map<String, Object> UnsupportedEncodingExceptionnHandler(UnsupportedEncodingException e) {
        this.result.put("message", e.getMessage());
        return this.result;
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    public Map<String, Object> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "\n");
        }

        this.result.put("message", strBuilder.toString());
        return this.result;
    }
}
