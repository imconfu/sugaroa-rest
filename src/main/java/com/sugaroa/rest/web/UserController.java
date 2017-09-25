package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.Menu;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import com.sugaroa.rest.service.MenuService;
import com.sugaroa.rest.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class UserController {
    private UserService service;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.service = userService;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users/{id}")
    public User get(@PathVariable Integer id) {
        return service.get(id);
    }

    @RequestMapping("/users")
    Page<User> findAll(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "mobile", required = false) String mobile,
            @Min(value = 2, message = "记录数必须大于2")
            @RequestParam(value = "rows", required = false) Integer size,
            @RequestParam(value = "page", required = false) Integer page) {

        if (page == null) page = 0;
        if (size == null) size = 50;

        System.out.println(page + "|" + size);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (account != null && !account.isEmpty()) {
                    predicates.add(cb.like(root.get("account"), "%" + account + "%"));
                }

                if (mobile != null && !mobile.isEmpty()) {
                    predicates.add(cb.like(root.get("mobile"), "%" + mobile + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User create(@RequestBody Map<String, Object> params) {
        return service.save(params);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public User update(@Min(value = 1, message = "用户ID必须大于1") @PathVariable Integer id, @RequestBody Map<String, Object> params) {
        System.out.println(id);
        return service.save(id, params);
    }
}
