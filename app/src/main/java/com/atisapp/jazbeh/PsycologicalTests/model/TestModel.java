package com.atisapp.jazbeh.PsycologicalTests.model;

import java.util.List;

public class TestModel {

    private int id;
    private String testId;
    private String name;
    private String description;
    private String image;
    private int questionCount;
    private int userCount;
    private List<QuestionModel> questionModelList;
    private List<ResultModel> resultModelList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public List<QuestionModel> getQuestionModelList() {
        return questionModelList;
    }

    public void setQuestionModelList(List<QuestionModel> questionModelList) {
        this.questionModelList = questionModelList;
    }

    public List<ResultModel> getResultModelList() {
        return resultModelList;
    }

    public void setResultModelList(List<ResultModel> resultModelList) {
        this.resultModelList = resultModelList;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
