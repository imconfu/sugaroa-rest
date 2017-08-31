package com.sugaroa.rest.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "oa_privilege")
@DynamicUpdate
public class Privilege extends SimpleTree{

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

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(relation, List.class);
        } catch (NullPointerException e) {
            //relation值为空时
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void setRelation(String relation) {
        this.relation = "[" + relation + "]";
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
