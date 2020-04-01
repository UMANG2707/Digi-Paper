package com.example.app3;

public class Result {
    String date, subject;
    int total_marks, scored_marks;

    public Result(String date, String subject, int total_marks, int scored_marks) {
        this.date = date;
        this.subject = subject;
        this.total_marks = total_marks;
        this.scored_marks = scored_marks;
    }

    public Result()
    {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(int total_marks) {
        this.total_marks = total_marks;
    }

    public int getScored_marks() {
        return scored_marks;
    }

    public void setScored_marks(int scored_marks) {
        this.scored_marks = scored_marks;
    }
}