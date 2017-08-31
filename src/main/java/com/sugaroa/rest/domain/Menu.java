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
    private String purviewArray;

    @Column(name = "purview_object")
    private String purviewObject;

    private String href;


    public String getPurviewArray() {
        return purviewArray;
    }

    public void setPurviewArray(String purviewArray) {
        this.purviewArray = purviewArray;
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
