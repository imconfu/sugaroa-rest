package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "oa_menu")
@DynamicUpdate
public class Menu {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer pid;

    @NotNull(message = "Path不能为空")
    private String path;

    @Column(nullable = false, name = "title")
    @NotNull(message = "名称不能为空")
    private String text;

    private int sort;

    @Column(name = "purview_array")
    private String purviewArray;

    @Column(name = "purview_object")
    private String purviewObject;

    private String href;

    private int status;

    private int deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", columnDefinition = "timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Transient
    private List<Menu> children;

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public List<Menu> getChildren() {
        return children;
    }


    public Integer getId() {
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

    public String getPurviewArray() {
        return purviewArray;
    }

    public void setPurviewArray(String purviewArray) {
        this.purviewArray = purviewArray;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getDeleted() {
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

    public Map<String, Integer> getPurviewObject() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(purviewObject, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setPurviewObject(String purviewObject) {
        this.purviewObject = purviewObject;
    }
}
