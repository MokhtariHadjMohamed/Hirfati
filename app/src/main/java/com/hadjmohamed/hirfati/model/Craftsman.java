package com.hadjmohamed.hirfati.model;

import java.util.List;

public class Craftsman extends User {

    private String craft, level, exYears, description;
    private Float rating;
    private List<String> works;

    public Craftsman() {
    }

    public Craftsman(String idUser, String name, String familyName, String address, String birthday,
                     String state, String city, String email, String phoneNumber, String userType,
                     String logInSituation, String craft, String level, String exYears,
                     String description, Float rating, List<String> works) {
        super(idUser, name, familyName, address, birthday, state, city, email, phoneNumber, userType, logInSituation);
        this.craft = craft;
        this.level = level;
        this.exYears = exYears;
        this.description = description;
        this.rating = rating;
        this.works = works;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExYears() {
        return exYears;
    }

    public void setExYears(String exYears) {
        this.exYears = exYears;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getWorks() {
        return works;
    }

    public void setWorks(List<String> works) {
        this.works = works;
    }
}
