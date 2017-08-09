package com.sugaroa.rest.security;

import com.sugaroa.rest.entity.User;
import com.sugaroa.rest.repository.UserRepository;
import com.sugaroa.rest.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    //@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with account '%s'.", username));
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        JwtUserDetails jwtUser = new JwtUserDetails(user.getId(), user.getAccount(), user.getPassword(), user.getAccount(), authorities);
        return jwtUser;
    }
}
