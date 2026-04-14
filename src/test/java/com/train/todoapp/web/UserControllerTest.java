package com.train.todoapp.web;

import com.train.todoapp.controller.UserController;
import com.train.todoapp.entity.dto.response.UserResponseDTO;
import com.train.todoapp.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @DisplayName("Создает пользователя")
    @Test
    void shouldCreateUser() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("Alice");

        when(userService.createUser(org.mockito.ArgumentMatchers.any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "Alice"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 1,
                            "username": "Alice"
                          }
                        """, false));
    }

    @DisplayName("Возвращает пользователя по идентификатору")
    @Test
    void shouldReturnUserById() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("Alice");

        when(userService.getById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                          {
                            "id": 1,
                            "username": "Alice"
                          }
                        """, false));
    }

    @DisplayName("Удаляет пользователя по идентификатору")
    @Test
    void shouldDeleteUserById() throws Exception {
        doNothing().when(userService).deleteById(2L);

        mockMvc.perform(delete("/api/v1/users/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
