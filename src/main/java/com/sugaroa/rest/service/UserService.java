package com.sugaroa.rest.service;

import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with account '%s'.", username));
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User jwtUser = new User(user.getId(), user.getAccount(), user.getPassword(), authorities);
        return jwtUser;
    }
}
