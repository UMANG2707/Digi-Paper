package com.example.app3;

public class Theoryquestion {
    String question,subject,id,weightage,chapter;


    public Theoryquestion(String question, String subject, String id, String weightage, String chapter) {
        this.question = question;
        this.subject = subject;
        this.id = id;
        this.weightage = weightage;
        this.chapter = chapter;
    }

    public Theoryquestion() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeightage() {
        return weightage;
    }

    public void setWeightage(String weightage) {
        this.weightage = weightage;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
