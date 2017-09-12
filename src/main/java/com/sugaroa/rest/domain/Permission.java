package com.sugaroa.rest.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "shiro_permission")
@DynamicUpdate
public class Permission extends Base implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Column(name = "parent_id")
    private Integer parentId;

    @NotNull(message = "Path不能为空")
    private String path;

    private String title;

    private String expression;

    private String url;

    private Boolean enabled = Boolean.FALSE;

    @ManyToMany
    @JoinTable(name = "shiro_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @Transient
    private List<Object> children;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Permission [id=" + id + ", title=" + title + ", path=" + path + ", url=" + url
                + ", expression=" + expression + ", parentId=" + parentId + ", enabled=" + enabled
                + ", roles=" + roles + "]";
    }
}
