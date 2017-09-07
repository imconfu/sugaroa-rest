package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "oa_user")
@DynamicUpdate
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private int id;

    @Column(updatable = false)
    private String account;

    private String mobile;

    private String realname;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String salt;

    private String remark;

    //接收该api传入参数用
    @Transient
    @JsonIgnore
    private String privileges;

    @Column(name = "purview_array")
    private String privilegeArray;

    @Column(name = "purview_object")
    private String privilegeObject;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private int deleted;

    //以下为UserDetails需要的
    @JsonIgnore
    @Transient
    private final Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    @Transient
    private String username;

    @JsonIgnore
    @Transient
    private Boolean accountNonExpired;

    @JsonIgnore
    @Transient
    private Boolean accountNonLocked;

    @JsonIgnore
    @Transient
    private Boolean credentialsNonExpired;

    @JsonIgnore
    @Transient
    private Boolean enabled;

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

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public Set<Integer> getPrivilegeArray() {
        if (privilegeArray == null || privilegeArray.isEmpty())
            return null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(privilegeArray, Set.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setPrivilegeArray(Set<Integer> privilegeSet) {
        if (privilegeSet == null || privilegeSet.size() == 0) {
            this.privilegeArray = null;
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.privilegeArray = mapper.writeValueAsString(privilegeSet);
        } catch (JsonProcessingException e) {
            this.privilegeArray = null;
        }
    }

    public Map<String, Integer> getPrivilegeObject() {
        if (privilegeObject == null || privilegeObject.isEmpty())
            return null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(privilegeObject, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setPrivilegeObject(Map<String, Integer> privilegeMap) {
        if (privilegeMap == null || privilegeMap.size() == 0) {
            this.privilegeObject = null;
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.privilegeObject = mapper.writeValueAsString(privilegeMap);
        } catch (JsonProcessingException e) {
            this.privilegeObject = null;
        }
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
