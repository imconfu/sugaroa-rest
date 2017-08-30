package com.sugaroa.rest.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "oa_menu")
@DynamicUpdate
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Integer pid;

    @Column(nullable = false, name = "title")
    private String text;

    @Column(name = "relation_array")
    private String relation;

    @Column(name = "purview_object")
    private String objectPurview;

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

    public Map<String, Integer> getObjectPurview() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(objectPurview, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setObjectPurview(String objectPurview) {
        this.objectPurview = objectPurview;
    }
}
