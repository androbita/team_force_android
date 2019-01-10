package com.app.salesapp.feedback;

public class PostFeedbackRequest {
    String token;
    String program_id;
    String picture;
    String description;
    String imagePath;
    String type;

    public PostFeedbackRequest(String token, String program_id, String description, String imagePath, String type) {
        this.token = token;
        this.program_id = program_id;
        this.description = description;
        this.imagePath = imagePath;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}


