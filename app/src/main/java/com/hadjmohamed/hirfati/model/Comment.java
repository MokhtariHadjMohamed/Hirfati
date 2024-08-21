package com.hadjmohamed.hirfati.model;

public class Comment {

    private String uid, uidUser, uidCraftsman;
    private String text;
    private float rate;

    public Comment() {
    }

    public Comment(String uid, String uidUser, String uidCraftsman, String text, float rate) {
        this.uid = uid;
        this.uidUser = uidUser;
        this.uidCraftsman = uidCraftsman;
        this.text = text;
        this.rate = rate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getUidCraftsman() {
        return uidCraftsman;
    }

    public void setUidCraftsman(String uidCraftsman) {
        this.uidCraftsman = uidCraftsman;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
