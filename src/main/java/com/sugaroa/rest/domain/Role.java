package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.ArrayList;
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

    //角色 <--> 权限关系: 多对多;
    @ManyToMany //(fetch = FetchType.EAGER) //立即从数据库中进行加载数据;
    @JoinTable(name = "shiro_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @JsonSerialize(using = PermissionListSerializer.class)
    private List<Permission> permissions;

    //角色 <--> 用户关系: 多对多;
    @ManyToMany
    @JoinTable(name = "shiro_user_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonSerialize(using = UserListSerializer.class)
    private List<User> users;// 一个角色对应多个用户

    @Column(columnDefinition = "bit DEFAULT b'1'", length = 1, insertable = false)
    protected Integer enabled;

    public Role() {
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Role(Integer id, String title) {
        this.id = id;
        this.title = title;
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

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
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

//    @Override
//    public String toString() {
//        return "Role [id=" + id + ",code=" + code + ",title=" + title + ",enabled=" + enabled
//                + ",permissions=" + permissions + ",users=" + users + "]";
//    }
}
