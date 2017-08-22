package com.sugaroa.rest.web;

import com.sugaroa.rest.Result;
import com.sugaroa.rest.domain.MenuService;
import com.sugaroa.rest.domain.User;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @RequestMapping("/menu/getusermenu")
    Result getUserMenu(@RequestAttribute("user") User user) throws JSONException {
        System.out.println("Request中的用户信息");
        System.out.println(user.getAccount());
        return new Result("userMenu", service.getUserMenu(user.getId()));
    }
}
