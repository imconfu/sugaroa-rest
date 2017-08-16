package com.sugaroa.rest.exception;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.sugaroa.rest.Result;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * AppExceptionHandler不能捕捉的异常在这里处理！
 */
@RestController
public class AppErrorController extends BasicErrorController {
    public AppErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    //@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        //{timestamp=Wed Aug 16 22:22:43 CST 2017, status=403, error=Forbidden, message=Access Denied, path=/user}
        //{timestamp=Thu Aug 17 01:42:48 CST 2017, status=500, error=Internal Server Error, exception=com.auth0.jwt.exceptions.SignatureVerificationException, message=The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256, path=/user, success=false}
        //body.clear();   //不要系统生成的定义
        body.put("success", false);

        if ( body.containsKey("exception") ){
            if(body.get("exception").equals(SignatureVerificationException.class.getName())){
                body.put("message", "签名验证失败");
            }
        }

        if(status == HttpStatus.FORBIDDEN){
            body.put("message", "没有访问权限");
        }

        System.out.println("ResponseEntity run"+body.toString());

        return new ResponseEntity<Map<String, Object>>(body, status);
    }
}
