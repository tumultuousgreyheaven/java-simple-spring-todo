package com.example.todo.controller;

import com.example.todo.dto.CreateTaskRequest;
import com.example.todo.dto.UpdateDescriptionTaskRequest;
import com.example.todo.dto.UpdateFullTaskRequest;
import com.example.todo.dto.UpdateStatusTaskRequest;
import com.example.todo.dto.UpdateTitleTaskRequest;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureRestTestClient
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private TaskService taskService;

    //--------------- findTaskById ---------------
    
    @Test
    void shouldReturn200AndTaskJson() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test1");

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc
            .perform(get("/tasks/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test1"));
    }

    @Test
    void shouldReturn404WhenNotFound() throws Exception {
        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc
            .perform(get("/tasks/99"))
            .andExpect(status().isNotFound());
    }

    //--------------- listTasks ---------------

    @Test
    void shouldReturn200AndListWhenNotEmpty() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("test1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("test2");

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc
            .perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("test1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("test2"));
    }

    @Test
    void shouldReturn200AndEmptyListWhenEmpty() throws Exception {   
        when(taskService.getAllTasks()).thenReturn(List.of());

        mockMvc
            .perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(0));
    }

    //--------------- create ---------------

    @Test
    void shouldReturn201AndCreatedTaskWhenCreated() throws Exception {
        CreateTaskRequest dto = new CreateTaskRequest();
        dto.setTitle("test1");
        dto.setDescription("test description");

        when(taskService.createTask("test1", "test description"))
            .thenReturn(new Task(dto));

        restTestClient
            .post().uri("/tasks").contentType(MediaType.APPLICATION_JSON).body(dto)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.title").isEqualTo("test1")
            .jsonPath("$.description").isEqualTo("test description");
    }

    //--------------- updateFull ---------------

    void shouldReturn200AndUpdatedTaskWhenFullUpdated() throws Exception {
        
    }

    void shouldReturn404WhenNoTaskToUpdateFull() throws Exception {

    }

    //--------------- updateTitle ---------------

    void shouldReturn200AndUpdatedTaskWhenTitleUpdated() throws Exception {
        
    }

    void shouldReturn404WhenNoTaskToUpdateTitle() throws Exception {
        
    }
    
    //--------------- updateDescription ---------------

    void shouldReturn200AndUpdatedTaskWhenDescriptionUpdated() throws Exception {
        
    }

    void shouldReturn404WhenNoTaskToUpdateDescription() throws Exception {
        
    }
    
    //--------------- updateStatus ---------------

    void shouldReturn200AndUpdatedTaskWhenStatusUpdated() throws Exception {
        
    }

    void shouldReturn404WhenNoTaskToUpdateStatus() throws Exception {
        
    }

    //--------------- deleteTask ---------------

    void shouldReturn204WhenDeleted() throws Exception {
        
    }
}
