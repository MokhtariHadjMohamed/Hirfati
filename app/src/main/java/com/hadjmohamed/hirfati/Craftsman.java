package com.hadjmohamed.hirfati;

import android.net.Uri;

import java.util.Date;

public class Craftsman {

    private String idC;
    private String name;
    private String familyName;
    private Date birthday;
    private Uri image;
    private String description;
    private String crafts;

    public Craftsman(String name, String familyName, Uri image, String description, String crafts) {
        this.name = name;
        this.familyName = familyName;
        this.image = image;
        this.description = description;
        this.crafts = crafts;
    }

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrafts() {
        return crafts;
    }

    public void setCrafts(String crafts) {
        this.crafts = crafts;
    }
}
