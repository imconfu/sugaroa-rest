package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "oa_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private int id;

    private String account;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @Transient
    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private String salt;

    @Column(name = "purview_array")
    private String purview_array;

    @Column(name = "purview_object")
    private String purview;

    private int deleted;

    public User() {
        this.authorities = null;
    }

    public User(
            int id,
            String account,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.authorities = authorities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    @Override
    public String getPassword() {
        return (password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return (salt);
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
