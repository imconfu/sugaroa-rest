package com.sugaroa.rest.web;

import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.MenuService;
import com.sugaroa.rest.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @RequestMapping("/menu/getusermenu")
    Result getUserMenu(@RequestAttribute("user") Map<String, Object> user) {
        System.out.println("用户信息");
System.out.println(user.toString());
        return new Result("userMenu", service.getUserMenu(1));
    }
}
