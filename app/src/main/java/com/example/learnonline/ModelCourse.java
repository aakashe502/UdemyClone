package com.example.learnonline;

public class ModelCourse {
    private String description,picc,timestamp,title,type;

    public ModelCourse() {
    }

    public ModelCourse(String description,String picc,String timestamp,String title,String type) {
        this.description = description;
        this.picc = picc;
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicc() {
        return picc;
    }

    public void setPicc(String picc) {
        this.picc = picc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
