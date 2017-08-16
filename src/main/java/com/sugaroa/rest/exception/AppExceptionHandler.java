package com.sugaroa.rest.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.sugaroa.rest.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * @ControllerAdvice 主要处理的就是 controller 层的错误信息，而没有进入 controller 层的错误 @ControllerAdvice 是无法处理的，
 * 那么我需要另外的一个全局错误处理，如AppErrorController。
 */
@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {
    /**
     * 处理参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<Result> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {

        System.out.println(e.getParameterName());
        Result result = new Result(-1, e.getParameterName() + "不能为空!");
        return new ResponseEntity<Result>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public Result AppExceptionHandler(AppException e) {
        return new Result(-1, e.getMessage());
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public Result UnsupportedEncodingExceptionnHandler(UnsupportedEncodingException e) {
        return new Result(-1, e.getMessage());
    }
}
