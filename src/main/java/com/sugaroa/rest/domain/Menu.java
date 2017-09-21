package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shiro_menu")
@DynamicUpdate
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Menu extends SimpleTree {
    @Transient
    private static final long serialVersionUID = 1L;

    //角色 <--> 权限关系: 多对多;
    @ManyToMany //(fetch = FetchType.EAGER) //立即从数据库中进行加载数据;
    @JoinTable(name = "shiro_menu_permission",
            joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @JsonSerialize(using = PermissionListSerializer.class)
    private List<Permission> permissions;

    private String href = "";

    protected Integer sort = 100;

    public Menu() {
    }

    public Menu(Integer id, Integer parentId, String text, String href) {
        super(id, parentId, text);
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
