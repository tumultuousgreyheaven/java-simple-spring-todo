package com.example.todo.dto;

import jakarta.validation.constraints.Size;

public class UpdateDescriptionTaskRequest {
    @Size(max = 500, message = "Description size must be at most 500 characters")
    private String description;

    public UpdateDescriptionTaskRequest() {}

    public UpdateDescriptionTaskRequest(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
