package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Privilege;
import com.sugaroa.rest.domain.PrivilegeService;
import com.sugaroa.rest.SimpleTree;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrivilegeController {

    private PrivilegeService service;

    public PrivilegeController(PrivilegeService service) {
        this.service = service;
    }

    @RequestMapping("/privileges")
    List<Privilege> getTree() {
        return service.getTree();
    }

    @RequestMapping("/privileges/combotree")
    List<SimpleTree> getComboTree() {
        return service.getComboTree();
    }

    @RequestMapping(value = "/privileges/{id}", method = RequestMethod.GET)
    public Privilege getOne(@PathVariable Integer id) {
        System.out.println("getOne");
        return service.findById(id);

    }
}
