package com.hadjmohamed.hirfati;

import android.net.Uri;

public class User {
    private String idUser, name, familyName, address, birthday,
            wilaiay, city, email, craft, level, exYears, userType, phoneNumber;

    public User() {
    }

    public User(String idUser, String name, String familyName, String address, String birthday,
                String wilaiay, String city, String email, String craft, String level, String exYears,
                String userType, String phoneNumber) {
        this.idUser = idUser;
        this.name = name;
        this.familyName = familyName;
        this.address = address;
        this.birthday = birthday;
        this.wilaiay = wilaiay;
        this.city = city;
        this.email = email;
        this.craft = craft;
        this.level = level;
        this.exYears = exYears;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWilaiay() {
        return wilaiay;
    }

    public void setWilaiay(String wilaiay) {
        this.wilaiay = wilaiay;
    }

    public String getCite() {
        return city;
    }

    public void setCite(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
