package com.hadjmohamed.hirfati;

import android.net.Uri;

import java.util.Date;
import java.util.List;

public class Craftsman extends User{

    private String craft, level, exYears, description;
    private List<String> works;

    public Craftsman() {
    }

    public Craftsman(String idUser, String name, String familyName, String address, String birthday,
                     String state, String city, String email, String userType, String logInSituation,
                     String craft, String level, String exYears, String description, List<String> works) {
        super(idUser, name, familyName, address, birthday, state, city, email, userType, logInSituation);
        this.craft = craft;
        this.level = level;
        this.exYears = exYears;
        this.description = description;
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
