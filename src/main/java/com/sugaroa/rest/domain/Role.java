package com.sugaroa.rest.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "shiro_role")
@DynamicUpdate
public class Role extends Base implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String title;

    private Boolean enabled = Boolean.FALSE;

    //角色 <--> 权限关系: 多对多;
    @ManyToMany(fetch = FetchType.EAGER) //立即从数据库中进行加载数据;
    @JoinTable(name = "shiro_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissions;

    //角色 <--> 用户关系: 多对多;
    @ManyToMany
    @JoinTable(name = "shiro_user_role", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;// 一个角色对应多个用户

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ",code=" + code + ",title=" + title + ",enabled=" + enabled
                + ",permissions=" + permissions + ",users=" + users + "]";
    }
}
