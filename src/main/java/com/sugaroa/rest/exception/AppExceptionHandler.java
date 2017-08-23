package com.sugaroa.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        result.put("success", false);
        result.put("code", -1);
        result.put("message", "用户信息");
        result.put("exception", "开发者信息");
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
        this.result.put("message", e.getParameterName() + "不能为空!");
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
}
