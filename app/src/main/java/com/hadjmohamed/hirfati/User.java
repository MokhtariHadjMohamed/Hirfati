package com.hadjmohamed.hirfati;

import android.net.Uri;

public class User {
    private String idUser, name, familyName, address, wilaiay, cite, email;
    private int phoneNumber;

    public User(String idUser, String name, String familyName, String address, String wilaiay, String cite, String email, int phoneNumber) {
        this.idUser = idUser;
        this.name = name;
        this.familyName = familyName;
        this.address = address;
        this.wilaiay = wilaiay;
        this.cite = cite;
        this.email = email;
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

    public String getWilaiay() {
        return wilaiay;
    }

    public void setWilaiay(String wilaiay) {
        this.wilaiay = wilaiay;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
