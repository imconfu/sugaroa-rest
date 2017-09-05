package com.sugaroa.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "oa_privilege")
@DynamicUpdate
public class Privilege extends SimpleTree {

    private String resource;

    private Integer operator;

    @Column(name = "relation_array")
    private String relation;

    private String action;

    private String remark;


    public Privilege() {
    }

    public Privilege(Integer id, Integer pid, String text) {
        super(id, pid, text);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public List<Integer> getRelation() {
        if (relation == null || relation.isEmpty())
            return null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(relation, List.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void setRelation(String relation) {
        if (relation == null || relation.isEmpty()) {
            this.relation = null;
            return;
        }

        String[] array = relation.split(",");
        Set<Integer> relationSet = new HashSet<Integer>();
        for (String id : array) {
            relationSet.add(Integer.valueOf(id));
        }

        if (relationSet.size() == 0){
            this.relation = null;
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.relation = mapper.writeValueAsString(relationSet);
        } catch (JsonProcessingException e) {
            this.relation = null;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
