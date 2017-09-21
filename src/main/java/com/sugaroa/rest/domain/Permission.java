package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "shiro_permission")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Permission extends SimpleTree {
    @Transient
    private static final long serialVersionUID = 1L;

    private String expression;

    private String url;

    @ManyToMany
    @JoinTable(name = "shiro_role_permission",
            joinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public Permission() {
    }

    public Permission(Integer id) {
        super(id);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Permission [id=" + id + ", text=" + text + ", path=" + path + ", url=" + url
                + ", expression=" + expression + ", parentId=" + parentId + ", enabled=" + enabled
                + ", roles=" + roles + "]";
    }
}
