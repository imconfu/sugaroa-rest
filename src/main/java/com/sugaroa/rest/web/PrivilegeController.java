package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Privilege;
import com.sugaroa.rest.service.PrivilegeService;
import com.sugaroa.rest.domain.SimpleTree;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class PrivilegeController {

    private PrivilegeService service;

    public PrivilegeController(PrivilegeService service) {
        this.service = service;
    }

    @RequestMapping("/privileges")
    List<Privilege> getTree() {
        return service.getTree();
    }

    /**
     * 获取树形结构，只包含:id,pid,text
     *
     * @return
     */
    @RequestMapping("/privileges/combotree")
    List<SimpleTree> getComboTree() {
        return service.getComboTree();
    }

    /**
     * 获取id=>text键值对
     *
     * @return
     */
    @RequestMapping("/privileges/pairs")
    Map<Integer, String> getPairs() {
        return service.getPairs();
    }

    @RequestMapping(value = "/privileges/{id}", method = RequestMethod.GET)
    public Privilege get(@PathVariable Integer id) {
        System.out.println("getOne");
        return service.get(id);
    }

    @RequestMapping(value = "/privileges", method = RequestMethod.POST)
    public Privilege create(HttpServletRequest request) {
        System.out.println("insertOne");
        return service.save(request.getParameterMap());
    }

    /**
     * 注意：@RequestBody使用时：Content-Type:application/json
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/privileges/{id}", method = RequestMethod.POST)
    public Privilege update(@Min(value = 2,message = "age必须大于2") @PathVariable Integer id, HttpServletRequest request) {

        System.out.println("updateOne");
        return service.save(id, request.getParameterMap());
    }
}
