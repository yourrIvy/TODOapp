package com.train.todoapp.controller;

import com.train.todoapp.entity.dto.request.UserRequestDTO;
import com.train.todoapp.entity.dto.response.UserResponseDTO;
import com.train.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API для работы с пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создание пользователя")
    @ApiResponse(responseCode = "200", description = "Успешно")
    public UserResponseDTO createUser(
            @RequestBody @Valid UserRequestDTO userRequestDTO
    ) {
        return userService.createUser(userRequestDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public UserResponseDTO getUserById(
            @Parameter(description = "ID пользователя")
            @PathVariable Long id
    ) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public void deleteUserById(
            @Parameter(description = "ID пользователя")
            @PathVariable Long id
    ) {
        userService.deleteById(id);
    }
}
