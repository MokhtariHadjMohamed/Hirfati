package com.hadjmohamed.hirfati;

import android.net.Uri;

public class User {
    private String Uid, name, familyName;
    private Uri image;

    public User(String uid, String name, String familyName, Uri image) {
        Uid = uid;
        this.name = name;
        this.familyName = familyName;
        this.image = image;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
