package com.labdesoft.roteiro1.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labdesoft.roteiro1.controller.TaskController;
import com.labdesoft.roteiro1.entity.Task;
import com.labdesoft.roteiro1.enums.TaskPriority;
import com.labdesoft.roteiro1.enums.TaskType;
import com.labdesoft.roteiro1.service.TaskService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(controllers = TaskController.class)
@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    public void init () {
        task = new Task();
        task.setDescription("Descriacao de teste para task");
        task.setType(TaskType.LIVRE);
        task.setPriority(TaskPriority.MEDIA);
        task.setCompleted(false);
    }

    @Test
    public void TaskController_CreateTask_ReturnCreated () throws Exception {
        // Mocking the service behavior
        given(taskService.create(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        // Performing an HTTP POST request to create a task
        ResultActions response = mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is("LIVRE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Descriacao de teste para task")));
    }

    @Test
    public void TaskController_ListAllTask_ReturnAllTasks () throws Exception {
        // Mocking tasks
        List<Task> tasks = new ArrayList<>();
        Task task2 = new Task();
        task2.setDescription("Descrição de teste para task 2");
        task2.setType(TaskType.LIVRE);
        task2.setPriority(TaskPriority.BAIXA);
        task2.setCompleted(false);

        tasks.add(task);
        tasks.add(task2);

        // Mocking the service behavior
        when(taskService.listAll()).thenReturn(tasks);

        // Performing an HTTP GET request to list all tasks
        ResultActions response = mockMvc.perform(get("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(tasks.size())));
    }

    @Test
    public void TaskController_FindOneTask_ReturnATask () throws Exception {
        // Mocking task id
        long id = 1L;

        // Mocking the service behavior
        when(taskService.findById(id)).thenReturn(task);

        // Performing an HTTP POST request to create a task
        ResultActions response = mockMvc.perform(get("/task/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is("LIVRE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Descriacao de teste para task")));
    }

    @Test
    public void TaskController_UpdateTask_ReturnATask () throws Exception {
        // Mocking task id
        long id = 1L;

        // Mocking the service behavior
        task.setCompleted(true);
        when(taskService.conclude(id)).thenReturn(task);

        // Performing an HTTP POST request to create a task
        ResultActions response = mockMvc.perform(put("/task/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is("LIVRE")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is("Descriacao de teste para task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed", CoreMatchers.is(true)));
    }
}