package com.atisapp.jazbeh.Questionnaire;

public class QuestionModel {


    /**
     * id : 5bb1dcdc0a9cf20c68dfc8c7
     * age : true
     * heartDisease : true
     * epilepsy : true
     * hysteria : true
     * bipolar : true
     * hearing : true
     * updatedAt : 2018-10-01T08:37:48.718Z
     * createdAt : 2018-10-01T08:37:48.718Z
     */

    private String id;
    private boolean age;
    private boolean heartDisease;
    private boolean epilepsy;
    private boolean hysteria;
    private boolean bipolar;
    private boolean hearing;
    private String updatedAt;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getAge() {
        return age;
    }

    public void setAge(boolean age) {
        this.age = age;
    }

    public boolean isHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public boolean isEpilepsy() {
        return epilepsy;
    }

    public void setEpilepsy(boolean epilepsy) {
        this.epilepsy = epilepsy;
    }

    public boolean isHysteria() {
        return hysteria;
    }

    public void setHysteria(boolean hysteria) {
        this.hysteria = hysteria;
    }

    public boolean isBipolar() {
        return bipolar;
    }

    public void setBipolar(boolean bipolar) {
        this.bipolar = bipolar;
    }

    public boolean isHearing() {
        return hearing;
    }

    public void setHearing(boolean hearing) {
        this.hearing = hearing;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
