package com.example.final_project_client;

import java.util.Date;
import java.util.List;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String token;
    private String refreshToken;
    private Date tokenExpires;
    private Date refreshTokenExpires;
    private String fullName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
    private String image;
    private String bio;
    private List<Subject> enrolledSubjects;
    private List<Quiz> createdQuizzes;

    public User() {
        // Default constructor
    }

    public User(String username, String password, String email, String role, String token, String refreshToken,
                Date tokenExpires, Date refreshTokenExpires, String fullName, String address, String emailAddress,
                String phoneNumber, String image, String bio) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpires = tokenExpires;
        this.refreshTokenExpires = refreshTokenExpires;
        this.fullName = fullName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(Date tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    public Date getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(Date refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void setEnrolledSubjects(List<Subject> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }

    public List<Quiz> getCreatedQuizzes() {
        return createdQuizzes;
    }

    public void setCreatedQuizzes(List<Quiz> createdQuizzes) {
        this.createdQuizzes = createdQuizzes;
    }


    // Subject and Quiz classes should also be defined in your project to match the server models
    public static class Subject {
        // Define Subject properties
    }

    public static class Quiz {
        // Define Quiz properties
    }
}
