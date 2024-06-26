package com.example.final_project_client;

import java.io.Serializable;

public class Question implements Serializable {
    private int id;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;
    private String subject;
    private String complexity;
    private int complexityInt;

    // Constructors, getters, and setters

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexityInt(int complexityInt) {
        this.complexityInt = complexityInt - 1;
    }

    public int getComplexityInt() {
        return complexityInt;
    }


    public String getQuestionText() {
        return question;
    }



    public int getQuestionId() {
        return id;
    }
}
