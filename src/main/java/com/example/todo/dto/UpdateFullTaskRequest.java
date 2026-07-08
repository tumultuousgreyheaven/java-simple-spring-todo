package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateFullTaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title size must be at most 100 characters")
    private String title;

    @Size(max = 500, message = "Description size must be at most 500 characters")
    private String description;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "TODO|IN_PROGRESS|DONE", message = "Status must be one of: TODO, IN_PROGRESS, DONE")
    private String status;

    public UpdateFullTaskRequest(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}