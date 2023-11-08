package com.hadjmohamed.hirfati.Admin;

import android.net.Uri;

import java.util.HashMap;

public class Report {

    private String idReport, idCraftsman, idUser, desc;
    private boolean readSituation;

    public Report() {
    }

    public Report(String idReport, String idCraftsman, String idUser, String desc, boolean readSituation) {
        this.idReport = idReport;
        this.idCraftsman = idCraftsman;
        this.idUser = idUser;
        this.desc = desc;
        this.readSituation = readSituation;
    }

    public String getIdReport() {
        return idReport;
    }

    public void setIdReport(String idReport) {
        this.idReport = idReport;
    }

    public String getIdCraftsman() {
        return idCraftsman;
    }

    public void setIdCraftsman(String idCraftsman) {
        this.idCraftsman = idCraftsman;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isReadSituation() {
        return readSituation;
    }

    public void setReadSituation(boolean readSituation) {
        this.readSituation = readSituation;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idReport", idReport);
        hashMap.put("idUser", idUser);
        hashMap.put("idCraftsman", idCraftsman);
        hashMap.put("desc", desc);
        hashMap.put("readSituation", readSituation);
        return hashMap;
    }

}
