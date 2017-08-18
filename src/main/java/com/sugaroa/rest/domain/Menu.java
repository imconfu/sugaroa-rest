package com.sugaroa.rest.domain;

import javax.persistence.*;

@Entity
@Table(name = "oa_menu")
public class Menu {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false, name = "title")
    private String text;

    @Column(name = "relation_array")
    private String relation;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return (text + "后缀");
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
}
