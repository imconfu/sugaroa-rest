package com.sugaroa.rest.web;

import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/user")
    Page<User> findAll() {
        int page = 0, size = 10;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    User update(@PathVariable Integer id, @RequestParam String password) {
        //User user = userRepository.findOne(id);

        User update = new User();
        update.setId(id);
        update.setPassword(password);
        update.notify();
        //BeanUtils.copyProperties(update, user);
        //System.out.println(find.getAccount());
        User save = userRepository.save(update);
        return save;
    }

    @RequestMapping("/user/create")
    String create() {
        User user = new User();
        user.setAccount("newaccount");
        user.setPassword("newpass");
        User save = userRepository.save(user);

        return null;
    }
}
