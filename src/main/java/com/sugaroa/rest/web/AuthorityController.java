package com.sugaroa.rest.web;

import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.AuthorityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityController {

    private AuthorityService service;

    public AuthorityController(AuthorityService service) {
        this.service = service;
    }

    @RequestMapping("/authorities")
    Result getTree() {
        return new Result("authorities", service.getTree());
    }
}
