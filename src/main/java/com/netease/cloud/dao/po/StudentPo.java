package com.netease.cloud.dao.po;

public class StudentPo {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;

    @Override
    public String toString() {
        return "StudentPo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
