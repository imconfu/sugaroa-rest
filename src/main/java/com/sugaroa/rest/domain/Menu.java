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
public class Menu extends SimpleTree{

    @Column(name = "purview_array")
    private String privilegeArray;

    @Column(name = "purview_object")
    private String privilegeObject;

    private String href;

    public Menu() {
    }

    public Menu(Integer id, Integer pid, String text, String href) {
        super(id, pid, text);
        this.href = href;
    }

    public String getPrivilegeArray() {
        return privilegeArray;
    }

    public void setPrivilegeArray(String privilegeArray) {
        this.privilegeArray = privilegeArray;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Map<String, Integer> getPrivilegeObject() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(privilegeObject, Map.class);
        } catch (NullPointerException e) {
            //relation值为空时
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void setPrivilegeObject(String privilegeObject) {
        this.privilegeObject = privilegeObject;
    }
}
