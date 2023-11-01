package com.hadjmohamed.hirfati;

public class State {
    private String ar_name, code, name;
    private int id;

    public State() {
    }

    public State(String ar_name, String code, int id, String name) {
        this.ar_name = ar_name;
        this.code = code;
        this.id = id;
        this.name = name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
}
