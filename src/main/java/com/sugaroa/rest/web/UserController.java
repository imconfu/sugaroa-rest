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
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

@RestController
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
    Page<User> findAll( HttpServletRequest request) {
        int page = 0, size = 100;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate criteria = null;
                criteria = cb.like(root.get("account"), "%con%");
                //query.where(resultPre);
                criteria = cb.and(criteria, cb.equal(root.get("deleted"), 0));

                //query.where(criteria)或用return criteria
                return null;
            }
        }, pageable);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User create(HttpServletRequest request) {
        return service.save(request.getParameterMap());
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public User update(@Min(value = 1, message = "用户ID必须大于1") @PathVariable Integer id, HttpServletRequest request) {
        System.out.println(id);
        System.out.println(request.getParameterMap());
        return service.save(id, request.getParameterMap());
    }
}
