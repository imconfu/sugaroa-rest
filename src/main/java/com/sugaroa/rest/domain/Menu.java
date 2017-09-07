package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "oa_menu")
@DynamicUpdate
public class Menu extends SimpleTree {

    //接收该api传入参数用
    @Transient
    @JsonIgnore
    private String privileges;

    @Column(name = "purview_array")
    private String privilegeArray;

    @Column(name = "purview_object")
    private String privilegeObject;

    private String href;

    public Menu() {
    }

    public Menu(Integer id, Integer pid, String text, String href, String privilegeArray) {
        super(id, pid, text);
        this.href = href;
        this.privilegeArray = privilegeArray;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public Set<Integer> getPrivilegeArray() {
        System.out.println("menu privilegeArray");
        System.out.println(privilegeArray);
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
}
