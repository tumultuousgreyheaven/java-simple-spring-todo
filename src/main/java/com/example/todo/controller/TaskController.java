package com.example.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.UpdateDescriptionTaskRequest;
import com.example.todo.dto.UpdateFullTaskRequest;
import com.example.todo.dto.UpdateStatusTaskRequest;
import com.example.todo.dto.UpdateTitleTaskRequest;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;

import jakarta.validation.Valid;

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
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody CreateTaskRequest request) {
        return service.createTask(request.getTitle(), request.getDescription());
    }

    /*
    *   As I have id path variable
    *   I am not able to create new resource
    *   with a PUT-request. Service returns
    *   404 Not Found for inexistent ids.
    */
    @PutMapping("/{id}")
    public Task updateFull(
        @PathVariable Long id, @Valid @RequestBody UpdateFullTaskRequest dto
    ) {
        return service.updateTaskFull(id, dto);
    }

    @PatchMapping("/{id}/title")
    public Task updateTitle(
        @PathVariable Long id, @Valid @RequestBody UpdateTitleTaskRequest dto
    ) {
        return service.updateTaskTextField(id, dto.getTitle(), "title");
    }

    @PatchMapping("/{id}/description")
    public Task updateDescription(
        @PathVariable Long id, @Valid @RequestBody UpdateDescriptionTaskRequest dto
    ) {
        return service.updateTaskTextField(id, dto.getDescription(), "description");
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(
        @PathVariable Long id, @Valid @RequestBody UpdateStatusTaskRequest dto
    ) {
        return service.updateTaskTextField(id, dto.getStatus(), "status");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
