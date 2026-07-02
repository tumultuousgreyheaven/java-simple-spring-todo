package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateTitleTaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title size must be at most 100 characters")
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
