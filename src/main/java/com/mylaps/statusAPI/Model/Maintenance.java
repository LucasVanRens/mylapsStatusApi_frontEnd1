package com.mylaps.statusAPI.Model;

public class Maintenance extends SystemObject {
    private String beginDate;
    private String endDate;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(final String beginDateM) {
        this.beginDate = beginDateM;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDateM) {
        this.endDate = endDateM;
    }
}