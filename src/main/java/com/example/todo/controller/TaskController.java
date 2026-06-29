package com.example.todo.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import com.example.todo.dto.CreateTaskRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> listTasks() {
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task findTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @PostMapping
    public Task create(@Valid @RequestBody CreateTaskRequest request) {
        // add creation in service, add input dto
    }
}
