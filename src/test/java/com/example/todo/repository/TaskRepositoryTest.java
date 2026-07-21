package com.example.todo.repository;

import com.example.todo.dto.UpdateFullTaskRequest;
import com.example.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(TaskRepository.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.sql.init.mode=always")
class TaskRepositoryTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2026-01-15T12:00:00Z");

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");

    @Autowired
    private TaskRepository taskRepository;

    //--------------- findAll ---------------

    @Test
    void shouldFindAllTasks() {
        Task task1 = persistTask("task 1", "desc 1", "TODO");
        Task task2 = persistTask("task 2", "desc 2", "DONE");

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(2);
        assertThat(tasks)
                .extracting("id")
                .containsExactlyInAnyOrder(task1.getId(), task2.getId());
    }

    @Test
    void shouldReturnEmptyListWhenNoTasks() {
        assertThat(taskRepository.findAll()).isEmpty();
    }

    //--------------- findById ---------------

    @Test
    void shouldFindTaskByIdWhenExists() {
        Task saved = persistTask("task 1", "desc 1", "TODO");

        Optional<Task> found = taskRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
        assertThat(found.get().getTitle()).isEqualTo("task 1");
        assertThat(found.get().getDescription()).isEqualTo("desc 1");
        assertThat(found.get().getStatus()).isEqualTo("TODO");
        assertThat(found.get().getCreatedAt()).isEqualTo(dbCompatibleInstant(FIXED_INSTANT));
    }

    @Test
    void shouldReturnEmptyWhenTaskNotFoundById() {
        assertThat(taskRepository.findById(999L)).isEmpty();
    }

    //--------------- save ---------------

    @Test
    void shouldSaveTaskAndAssignId() {
        Task task = new Task();
        task.setTitle("new task");
        task.setDescription("new description");
        task.setStatus("IN_PROGRESS");
        task.setCreatedAt(FIXED_INSTANT);

        Task saved = taskRepository.save(task);

        assertThat(saved.getId()).isNotNull();

        Task found = taskRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("new task");
        assertThat(found.getDescription()).isEqualTo("new description");
        assertThat(found.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(found.getCreatedAt()).isEqualTo(dbCompatibleInstant(FIXED_INSTANT));

        assertThat(saved.getTitle()).isEqualTo("new task");
        assertThat(saved.getDescription()).isEqualTo("new description");
        assertThat(saved.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(saved.getCreatedAt()).isEqualTo(FIXED_INSTANT);
    }

    //--------------- updateFull ---------------

    @Test
    void shouldUpdateFullTaskWhenExists() {
        Task saved = persistTask("old title", "old desc", "TODO");
        UpdateFullTaskRequest dto = new UpdateFullTaskRequest("new title", "new desc", "DONE");

        Optional<Task> updated = taskRepository.updateFull(saved.getId(), dto);

        assertThat(updated).isPresent();
        assertThat(updated.get().getId()).isEqualTo(saved.getId());
        assertThat(updated.get().getTitle()).isEqualTo("new title");
        assertThat(updated.get().getDescription()).isEqualTo("new desc");
        assertThat(updated.get().getStatus()).isEqualTo("DONE");
    }

    @Test
    void shouldReturnEmptyWhenUpdateFullForMissingId() {
        UpdateFullTaskRequest dto = new UpdateFullTaskRequest("title", "desc", "TODO");

        assertThat(taskRepository.updateFull(999L, dto)).isEmpty();
    }

    //--------------- updateText ---------------

    @Test
    void shouldUpdateTitleWhenExists() {
        Task saved = persistTask("old title", "desc", "TODO");

        Optional<Task> updated = taskRepository.updateText(saved.getId(), "new title", "title");

        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("new title");
        assertThat(updated.get().getDescription()).isEqualTo("desc");
        assertThat(updated.get().getStatus()).isEqualTo("TODO");
    }

    @Test
    void shouldUpdateDescriptionWhenExists() {
        Task saved = persistTask("title", "old desc", "TODO");

        Optional<Task> updated = taskRepository.updateText(saved.getId(), "new desc", "description");

        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("title");
        assertThat(updated.get().getDescription()).isEqualTo("new desc");
        assertThat(updated.get().getStatus()).isEqualTo("TODO");
    }

    @Test
    void shouldUpdateStatusWhenExists() {
        Task saved = persistTask("title", "desc", "TODO");

        Optional<Task> updated = taskRepository.updateText(saved.getId(), "IN_PROGRESS", "status");

        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("title");
        assertThat(updated.get().getDescription()).isEqualTo("desc");
        assertThat(updated.get().getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldReturnEmptyWhenUpdateTextForMissingId() {
        assertThat(taskRepository.updateText(999L, "text", "title")).isEmpty();
    }

    //--------------- deleteTask ---------------

    @Test
    void shouldDeleteTaskWhenExists() {
        Task saved = persistTask("task 1", "desc", "TODO");

        taskRepository.deleteTask(saved.getId());

        assertThat(taskRepository.findById(saved.getId())).isEmpty();
    }

    private Task persistTask(String title, String description, String status) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreatedAt(FIXED_INSTANT);
        return taskRepository.save(task);
    }

    private static Instant dbCompatibleInstant(Instant instant) {
        return instant.truncatedTo(ChronoUnit.MICROS);
    }
}
