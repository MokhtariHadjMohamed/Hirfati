package com.hadjmohamed.hirfati;

public class Crafts {
    private String uid;
    private String name;
    private String desc;

    public Crafts() {
    }

    public Crafts(String uid, String name, String desc) {
        this.uid = uid;
        this.name = name;
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
