package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Privilege;
import com.sugaroa.rest.domain.PrivilegeService;
import com.sugaroa.rest.SimpleTree;
import org.hibernate.EmptyInterceptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping("/privileges/keyvaluepairs")
    Map<Integer, String> getKeyValuePairs() {
        return service.getKeyValuePairs();
    }

    @RequestMapping(value = "/privileges/{id}", method = RequestMethod.GET)
    public Privilege getOne(@PathVariable Integer id) {
        System.out.println("getOne");
        return service.findById(id);
    }

    @RequestMapping(value = "/privileges", method = RequestMethod.POST)
    public Privilege insertOne() {
        System.out.println("getOne");
        return null;
    }

    /**
     * 注意：@RequestBody使用时：Content-Type:application/json
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/privileges/{id}", method = RequestMethod.POST)
    public Privilege updateOne(@PathVariable Integer id, HttpServletRequest request) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("id", id);
//        if(request.getParameter(""))
        //request.getParameterMap().containsKey()
        /**
         * 使用BeanWrapper动态调用 setter 方法赋值,还可以自动转换类型！！！
         * 有值入的值不管是不是为空，都更新，没传的不更新
         * dynamicUpdate还没试出来。。。？？？？
         */
        service.update(id, request.getParameterMap());

        System.out.println(request.getParameterMap().size());
        Privilege p = new Privilege();
        BeanWrapper bw = new BeanWrapperImpl(p);
        System.out.println(p.getText());
        System.out.println(p.getPid());
        bw.setPropertyValue("text", "abc");
        bw.setPropertyValue("pid", "2131");
        System.out.println(p.getText());
        System.out.println(p.getPid());
        return service.findById(id);
    }
}
