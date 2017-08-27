package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.DynamicUpdate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "oa_user")
//@DynamicUpdate
@org.hibernate.annotations.Entity(dynamicUpdate =true)
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private int id;

    @Column(updatable = false)
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP", updatable = false)
    private Date createTime;

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

    public Map<String, Integer> getPurview() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(purview, Map.class);
        } catch (NullPointerException e) {
            //relation值为空时
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void setPurview(String purview) {
        this.purview = purview;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
