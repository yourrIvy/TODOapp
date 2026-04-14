package com.train.todoapp.web;

import com.train.todoapp.controller.TaskController;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {TaskController.class})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnTaskById() throws Exception {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);
        taskResponseDTO.setTitle("Test Task");

        when(taskService.getById(1L)).thenReturn(taskResponseDTO);

        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 1,
                            "title": "Test Task"
                          }
                        """, false));
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(2L);
        taskResponseDTO.setTitle("New Task");
        taskResponseDTO.setDescription("New task description");
        taskResponseDTO.setCompleted(false);

        when(taskService.createTask(org.mockito.ArgumentMatchers.any())).thenReturn(taskResponseDTO);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "New Task",
                                  "description": "New task description",
                                  "completed": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 2,
                            "title": "New Task",
                            "description": "New task description",
                            "completed": false
                          }
                        """, false));
    }

    @Test
    void shouldReturnPagedTasks() throws Exception {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(3L);
        taskResponseDTO.setTitle("Paged Task");

        when(taskService.getAll(org.mockito.ArgumentMatchers.any())).thenReturn(new PageImpl<>(List.of(taskResponseDTO)));

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "content": [
                              {
                                "id": 3,
                                "title": "Paged Task"
                              }
                            ],
                            "totalElements": 1
                          }
                        """, false));
    }

    @Test
    void shouldUpdateTaskById() throws Exception {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(4L);
        taskResponseDTO.setTitle("Updated Task");
        taskResponseDTO.setCompleted(true);

        when(taskService.updateById(org.mockito.ArgumentMatchers.eq(4L), org.mockito.ArgumentMatchers.any())).thenReturn(taskResponseDTO);

        mockMvc.perform(put("/api/v1/tasks/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Task",
                                  "description": "Updated description",
                                  "completed": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 4,
                            "title": "Updated Task",
                            "completed": true
                          }
                        """, false));
    }

    @Test
    void shouldPatchTaskById() throws Exception {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(5L);
        taskResponseDTO.setTitle("Patched Task");
        taskResponseDTO.setCompleted(true);

        when(taskService.patchTaskById(org.mockito.ArgumentMatchers.eq(5L), org.mockito.ArgumentMatchers.any())).thenReturn(taskResponseDTO);

        mockMvc.perform(patch("/api/v1/tasks/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "completed": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 5,
                            "title": "Patched Task",
                            "completed": true
                          }
                        """, false));
    }

    @Test
    void shouldDeleteTaskById() throws Exception {
        doNothing().when(taskService).deleteById(6L);

        mockMvc.perform(delete("/api/v1/tasks/6"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
