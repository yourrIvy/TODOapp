package com.train.todoapp.web;

import com.train.todoapp.controller.TaskListController;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.service.TaskListService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {TaskListController.class})
public class TaskListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskListService taskListService;

    @DisplayName("Возвращает список задач по идентификатору")
    @Test
    void shouldReturnTaskListById() throws Exception {
        TaskListResponseDTO responseDTO = new TaskListResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("My List");
        responseDTO.setAuthorId(10L);
        responseDTO.setExecutorId(20L);

        when(taskListService.getById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/lists/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 1,
                            "name": "My List",
                            "authorId": 10,
                            "executorId": 20
                          }
                        """, false));
    }

    @DisplayName("Создает список задач")
    @Test
    void shouldCreateTaskList() throws Exception {
        TaskListResponseDTO responseDTO = new TaskListResponseDTO();
        responseDTO.setId(2L);
        responseDTO.setName("New List");
        responseDTO.setAuthorId(11L);
        responseDTO.setExecutorId(21L);

        when(taskListService.createTaskList(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "New List",
                                  "authorId": 11,
                                  "executorId": 21
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 2,
                            "name": "New List",
                            "authorId": 11,
                            "executorId": 21
                          }
                        """, false));
    }

    @DisplayName("Обновляет список задач по идентификатору")
    @Test
    void shouldUpdateTaskListById() throws Exception {
        TaskListResponseDTO responseDTO = new TaskListResponseDTO();
        responseDTO.setId(5L);
        responseDTO.setName("Updated List");
        responseDTO.setAuthorId(30L);
        responseDTO.setExecutorId(40L);

        when(taskListService.updateById(eq(5L), any(com.train.todoapp.entity.dto.request.TaskListRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/lists/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated List",
                                  "authorId": 30,
                                  "executorId": 40
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 5,
                            "name": "Updated List",
                            "authorId": 30,
                            "executorId": 40
                          }
                        """, false));
    }

    @DisplayName("Добавляет задачу в список")
    @Test
    void shouldAddTaskToListByIds() throws Exception {
        when(taskListService.addTaskToList(7L, 17L)).thenReturn(new TaskListResponseDTO());

        mockMvc.perform(post("/api/v1/lists/7/tasks/17"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @DisplayName("Удаляет задачу из списка")
    @Test
    void shouldRemoveTaskFromListByIds() throws Exception {
        when(taskListService.deleteTaskFromList(7L, 17L)).thenReturn(new TaskListResponseDTO());

        mockMvc.perform(delete("/api/v1/lists/7/tasks/17"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @DisplayName("Возвращает списки задач постранично")
    @Test
    void shouldReturnPagedTaskLists() throws Exception {
        TaskListResponseDTO responseDTO = new TaskListResponseDTO();
        responseDTO.setId(3L);
        responseDTO.setName("Paged List");
        responseDTO.setAuthorId(12L);
        responseDTO.setExecutorId(22L);

        when(taskListService.getAll(any())).thenReturn(new PageImpl<>(List.of(responseDTO)));

        mockMvc.perform(get("/api/v1/lists"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "content": [
                              {
                                "id": 3,
                                "name": "Paged List",
                                "authorId": 12,
                                "executorId": 22
                              }
                            ],
                            "totalElements": 1
                          }
                        """, false));
    }

    @DisplayName("Частично обновляет список задач по идентификатору")
    @Test
    void shouldPatchTaskListById() throws Exception {
        TaskListResponseDTO responseDTO = new TaskListResponseDTO();
        responseDTO.setId(5L);
        responseDTO.setName("Patched List");
        responseDTO.setAuthorId(14L);
        responseDTO.setExecutorId(24L);

        when(taskListService.patchById(eq(5L), any())).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/lists/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Patched List",
                                  "authorId": 14,
                                  "executorId": 24
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 5,
                            "name": "Patched List",
                            "authorId": 14,
                            "executorId": 24
                          }
                        """, false));
    }

    @DisplayName("Удаляет список задач по идентификатору")
    @Test
    void shouldDeleteTaskListById() throws Exception {
        doNothing().when(taskListService).deleteById(6L);

        mockMvc.perform(delete("/api/v1/lists/6"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
