package com.sugaroa.rest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "oa_privilege")
@DynamicUpdate
public class Privilege extends SimpleTree{

    private String resource;

    private int operator;

    @Column(name = "relation_array")
    private String relation;

    private String action;

    private int sort;

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

    public int getOperator() {
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
