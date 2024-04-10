package com.example.final_project_client;

public class Subject {

    private int id;
    private String SubjectName;
    private String SubjectDescription;
    private String SubjectImage;

    public Subject() {
        // Default constructor
    }

    public Subject(String SubjectName, String SubjectDescription, String SubjectImage) {
        this.SubjectName = SubjectName;
        this.SubjectDescription = SubjectDescription;
        this.SubjectImage = SubjectImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String SubjectName) {
        this.SubjectName = SubjectName;
    }

    public String getSubjectDescription() {
        return SubjectDescription;
    }

    public void setSubjectDescription(String SubjectDescription) {
        this.SubjectDescription = SubjectDescription;
    }

    public String getSubjectImage() {
        return SubjectImage;
    }

    public void setSubjectImage(String SubjectImage) {
        this.SubjectImage = SubjectImage;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", SubjectName='" + SubjectName + '\'' +
                ", SubjectDescription='" + SubjectDescription + '\'' +
                ", SubjectImage='" + SubjectImage + '\'' +
                '}';
    }
}
