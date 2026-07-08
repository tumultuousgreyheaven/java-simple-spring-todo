package com.example.todo.controller;

import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureRestTestClient
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

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



    //--------------- updateFull ---------------



    //--------------- updateTitle ---------------


    
    //--------------- updateDescription ---------------

    
    
    //--------------- updateStatus ---------------



    //--------------- deleteTask ---------------


}
