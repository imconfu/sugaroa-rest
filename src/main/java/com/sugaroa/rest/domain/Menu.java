package com.sugaroa.rest.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "oa_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Integer pid;

    @Column(nullable = false, name = "title")
    private String text;

    @Column(name = "relation_array")
    private String relation;
    private String href;

    private int status;

    private int deleted;

    @Transient
    private List<Menu> children;

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public List<Menu> getChildren() {
        return children;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
