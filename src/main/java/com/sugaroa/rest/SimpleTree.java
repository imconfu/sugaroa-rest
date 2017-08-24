package com.sugaroa.rest;

import java.util.List;

public class SimpleTree {
    private int id;

    private Integer pid;

    private String text;

    private List<SimpleTree> children;

    public SimpleTree(int id, Integer pid, String text) {
        super();
        this.id = id;
        this.pid = pid;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChildren(List<SimpleTree> children) {
        this.children = children;
    }

    public List<SimpleTree> getChildren() {
        return children;
    }
}
