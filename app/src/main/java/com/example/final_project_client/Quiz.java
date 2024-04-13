package com.example.final_project_client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Quiz {
    private String question;
    private String[] answers;
    private int correctAnswer;
    private int complexity;
    private String subject;

    public Quiz() {
        // Default constructor
    }

    public Quiz(String question, String[] answers, int correctAnswer, int complexity, String subject) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.complexity = complexity;
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "question='" + question + '\'' +
                ", answers=" + Arrays.toString(answers) +
                ", correctAnswer=" + correctAnswer +
                ", complexity=" + complexity +
                ", subject='" + subject + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"question\": \"" + question + "\"," +
                "\"answers\": " + Arrays.toString(answers) + "," +
                "\"correctAnswer\": " + correctAnswer + "," +
                "\"complexity\": " + complexity + "," +
                "\"subject\": \"" + subject + "\"" +
                "}";
    }
}
