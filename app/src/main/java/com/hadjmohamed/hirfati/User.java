package com.hadjmohamed.hirfati;

import android.net.Uri;

import java.util.HashMap;

public class User {
    private String idUser, name, familyName, address, birthday,
            state, city, email, phoneNumber, userType, logInSituation;

    public User() {
    }

    public User(String idUser, String name, String familyName, String address, String birthday,
                String state, String city, String email, String phoneNumber, String userType,
                String logInSituation) {
        this.idUser = idUser;
        this.name = name;
        this.familyName = familyName;
        this.address = address;
        this.birthday = birthday;
        this.state = state;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.logInSituation = logInSituation;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLogInSituation() {
        return logInSituation;
    }

    public void setLogInSituation(String logInSituation) {
        this.logInSituation = logInSituation;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idUser", idUser);
        hashMap.put("name", name);
        hashMap.put("familyName", familyName);
        hashMap.put("address", address);
        hashMap.put("birthday", birthday);
        hashMap.put("state", state);
        hashMap.put("city", city);
        hashMap.put("email", email);
        hashMap.put("phoneNumber", phoneNumber);
        hashMap.put("userType", userType);
        hashMap.put("logInSituation", logInSituation);
        return hashMap;
    }
}
