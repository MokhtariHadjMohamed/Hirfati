package com.hadjmohamed.hirfati.Admin;

import android.net.Uri;

public class Report {

    private String reporter, reported, desc;

    public Report(String reporter, String reported, String desc) {
        this.reporter = reporter;
        this.reported = reported;
        this.desc = desc;
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
}
