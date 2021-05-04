package com.atisapp.jazbeh.PsycologicalTests.model;

public class ResultModel {
    private int id;
    private String resultId;
    private String resultText;
    private int resultTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public int getResultTotal() {
        return resultTotal;
    }

    public void setResultTotal(int resultTotal) {
        this.resultTotal = resultTotal;
    }
}
