package com.train.todoapp.controller;

import com.train.todoapp.entity.dto.request.PatchTaskRequestDTO;
import com.train.todoapp.entity.dto.request.TaskRequestDTO;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "API для работы с задачами")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Создание задачи")
    @ApiResponse(responseCode = "200", description = "Успешно")
    public TaskResponseDTO createTask(
            @RequestBody @Valid TaskRequestDTO taskRequestDTO
    ) {
        return taskService.createTask(taskRequestDTO);
    }

    @GetMapping
    @Operation(summary = "Получение задач")
    @ApiResponse(responseCode = "200", description = "Успешно")
    public Page<TaskResponseDTO> getAllTasks(
            @PageableDefault(page = 0, size = 10)
            @ParameterObject
            Pageable pageable
    ) {
        return taskService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение задачи по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public TaskResponseDTO getTaskById(
            @Parameter(description = "ID задачи")
            @PathVariable Long id
    ) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление задачи по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public TaskResponseDTO updateTask(
            @Parameter(description = "ID задачи")
            @PathVariable Long id,
            @RequestBody @Valid TaskRequestDTO taskRequestDTO
    ) {
        return taskService.updateById(id, taskRequestDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление задачи по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public TaskResponseDTO patchTask(
            @Parameter(description = "ID задачи")
            @PathVariable Long id,
            @RequestBody @Valid PatchTaskRequestDTO patchTaskRequestDTO
    ) {
        return taskService.patchTaskById(id, patchTaskRequestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление задачи по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public void deleteTaskById(
            @Parameter(description = "ID задачи")
            @PathVariable Long id
    ) {
        taskService.deleteById(id);
    }
}
