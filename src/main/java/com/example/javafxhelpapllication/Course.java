package com.example.javafxhelpapllication;

public class Course {

    private String name;
    private String description;
    private String photo;
    private String progress;
    private String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public Course(String name, String description, String photo, String progress, String time){

        this.description = description;
        this.name = name;
        this.photo = photo;
        this.progress = progress;
        this.time = time;
    }
}
