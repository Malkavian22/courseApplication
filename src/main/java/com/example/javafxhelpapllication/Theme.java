package com.example.javafxhelpapllication;

public class Theme {

    private String name;
    private String type;
    private String progress;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Theme(String name, String type, String progress){

        this.name = name;
        this.type = type;
        this.progress = progress;
    }
}
