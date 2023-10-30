package com.hadjmohamed.hirfati.Admin;

import android.net.Uri;

import java.util.HashMap;

public class Report {

    private String idReport, reporter, reported, desc;
    private boolean readSituation;

    public Report() {
    }

    public Report(String idReport, String reporter, String reported, String desc, boolean readSituation) {
        this.idReport = idReport;
        this.reporter = reporter;
        this.reported = reported;
        this.desc = desc;
        this.readSituation = readSituation;
    }

    public String getIdReport() {
        return idReport;
    }

    public void setIdReport(String idReport) {
        this.idReport = idReport;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
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
        hashMap.put("reporter", reporter);
        hashMap.put("reported", reported);
        hashMap.put("desc", desc);
        hashMap.put("readSituation", readSituation);
        return hashMap;
    }

}
