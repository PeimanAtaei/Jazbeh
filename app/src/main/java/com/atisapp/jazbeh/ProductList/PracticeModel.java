package com.atisapp.jazbeh.ProductList;

import java.io.Serializable;

public class PracticeModel implements Serializable {

    public static final int IMAGE   = 0;
    public static final int PDF     = 1;
    public static final int VOICE   = 2;

    private int     id;
    private int     practiceType;
    private String  practiceId;
    private String  practiceURL;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(int practiceType) {
        this.practiceType = practiceType;
    }

    public String getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(String practiceId) {
        this.practiceId = practiceId;
    }

    public String getPracticeURL() {
        return practiceURL;
    }

    public void setPracticeURL(String practiceURL) {
        this.practiceURL = practiceURL;
    }
}
