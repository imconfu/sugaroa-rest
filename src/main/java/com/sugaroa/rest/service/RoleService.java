package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.Role;
import com.sugaroa.rest.domain.RoleRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleService {

    @Resource
    private RoleRepository repository;

    public Role get(Integer id) {
        return repository.findOne(id);
    }
}
