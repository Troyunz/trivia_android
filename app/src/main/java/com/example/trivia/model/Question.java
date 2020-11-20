package com.example.trivia.model;

public class Question {

    private String answer;
    private Boolean answertrue;


    public Question() {
        this.answer = answer;
    }

    public Question(String answer, Boolean answertrue) {
        this.answer = answer;
        this.answertrue = answertrue;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getAnswertrue() {
        return answertrue;
    }

    public void setAnswertrue(Boolean answertrue) {
        this.answertrue = answertrue;
    }
}
