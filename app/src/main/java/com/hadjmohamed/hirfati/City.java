package com.hadjmohamed.hirfati;

public class City {

    private int id;
    private String commune_name, daira_name, state_name, state_code;

    public City() {
    }

    public City(int id, String commune_name, String daira_name, String state_code, String state_name) {
        this.id = id;
        this.commune_name = commune_name;
        this.daira_name = daira_name;
        this.state_code = state_code;
        this.state_name = state_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommune_name() {
        return commune_name;
    }

    public void setCommune_name(String commune_name) {
        this.commune_name = commune_name;
    }

    public String getDaira_name() {
        return daira_name;
    }

    public void setDaira_name(String daira_name) {
        this.daira_name = daira_name;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
