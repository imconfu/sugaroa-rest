package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.Permission;
import com.sugaroa.rest.domain.SimpleTree;
import com.sugaroa.rest.service.MenuService;
import com.sugaroa.rest.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class PermissionController {

    @Autowired
    private PermissionService service;

    @RequestMapping("/permissions/pairs")
    Map<Integer, String> getPairs() {
        return service.getPairs();
    }


    @RequestMapping(value = "/permissions/{id}")
    public Permission get(@Min(value = 99, message = "权限ID必须大于99") @PathVariable Integer id) {
        return service.get(id);
    }

    @RequestMapping("/permissions/combotree")
    List<SimpleTree> getComboTree() {
        return service.getComboTree();
    }

    // annocation方式
    // 如：@RequiresAuthentication
    @RequestMapping(value = "/permissions", method = RequestMethod.POST)
    public Permission create(HttpServletRequest request) {
        return service.save(request.getParameterMap());
    }

    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.POST)
    public Permission update(@Min(value = 1, message = "权限ID必须大于1") @PathVariable Integer id, HttpServletRequest request) {
        return service.save(id, request.getParameterMap());
    }
    @RequestMapping("/permissions")
    List<Permission> getTree() {
        //基于字符串的权限检查
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.hasRole("market")) {
            System.out.println("May the Schwartz be with you!");
        } else {
            System.out.println("Hello, mere mortal.");
        }
        if (currentUser.isPermitted("printer:print:laserjet4400n")) {
            //show the Print button
            System.out.println("currentUser Permitted!");
        } else {
            //don't show the button?  Grey it out?
            System.out.println("currentUser NOT Permitted!");
        }
        return service.getTree();
    }
}
