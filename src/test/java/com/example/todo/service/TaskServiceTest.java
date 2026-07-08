package com.example.todo.service;

import com.example.todo.dto.UpdateFullTaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    //--------------- getTaskById ---------------

    @Test
    void shouldReturnTaskWhenExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertThat(result.getTitle()).isEqualTo("Test");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.getTaskById(99L)
        );
        assertThat(ex.getMessage()).contains("99");
    }

    //--------------- getAllTasks ---------------
    
    @Test
    void shouldReturnEmptyListWhenEmpty() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<Task> result = taskService.getAllTasks();

        assertThat(result.isEmpty()).isEqualTo(true);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAllTasksWhenExist() {
        List<Task> tasks = List.of(
            new Task(1L, "test1", "", "TODO", Instant.now()),
            new Task(2L, "test2", "second task", "DONE", Instant.now())
        );
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("test1");
        assertThat(result.get(0).getDescription()).isEqualTo("");
        assertThat(result.get(0).getStatus()).isEqualTo("TODO");

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getTitle()).isEqualTo("test2");
        assertThat(result.get(1).getDescription()).isEqualTo("second task");
        assertThat(result.get(1).getStatus()).isEqualTo("DONE");

        verify(taskRepository, times(1)).findAll();
    }

    //--------------- createTask ---------------

    @Test
    void shouldReturnSavedTaskWhenSucceed() {
        String title = "test1";
        String description = "test description";
        Task outTask = new Task(1L, title, description, "TODO", Instant.now());
        when(taskRepository.save(any(Task.class))).thenReturn(outTask);

        Task result = taskService.createTask(title, description);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getStatus()).isEqualTo("TODO");

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    //--------------- updateTaskFull ---------------

    @Test
    void shouldReturnUpdatedTaskWhenExistsUpdateFull() {
        Long id = 1L;
        String title = "test1";
        String description = "test description";
        UpdateFullTaskRequest dto = new UpdateFullTaskRequest(title, description, "TODO");
        when(taskRepository.updateFull(id, dto)).thenReturn(Optional.of(new Task(id, dto)));

        Task result = taskService.updateTaskFull(id, dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getStatus()).isEqualTo("TODO");

        verify(taskRepository, times(1)).updateFull(id, dto);
    }

    @Test
    void shouldThrowExceptionWhenNoTaskUpdateFull() {
        Long id = 99L;
        String title = "test1";
        String description = "test description";
        UpdateFullTaskRequest dto = new UpdateFullTaskRequest(title, description, "TODO");
        when(taskRepository.updateFull(id, dto)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.updateTaskFull(id, dto)
        );
        assertThat(ex.getMessage()).contains("99");
    }

    //--------------- updateTaskTextField ---------------

    @Test
    void shouldReturnUpdatedTaskWhenExistsUpdateText() {
        Long id = 1L;
        String title = "test1";
        String description = "test description";
        when(taskRepository.updateText(id, title, "title")).thenReturn(
            Optional.of(new Task(id, title, description, "TODO", Instant.now()))
        );

        Task result = taskService.updateTaskTextField(id, title, "title");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getStatus()).isEqualTo("TODO");

        verify(taskRepository, times(1)).updateText(id, title, "title");
    }

    @Test
    void shouldThrowExceptionWhenNoTaskUpdateText() {
        Long id = 99L;
        String title = "test1";
        when(taskRepository.updateText(id, title, "title")).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(
            TaskNotFoundException.class,
            () -> taskService.updateTaskTextField(id, title, "title")
        );
        assertThat(ex.getMessage()).contains("99");
    }

}