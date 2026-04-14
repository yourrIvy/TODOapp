package com.train.todoapp.controller;

import com.train.todoapp.entity.dto.request.PatchTaskListRequestDTO;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.service.TaskListService;

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
@RequestMapping("/api/v1/lists")
@RequiredArgsConstructor
@Tag(name = "TaskLists", description = "API для работы со списками задач")
public class TaskListController {

    private final TaskListService taskListService;

    @PostMapping
    @Operation(summary = "Создание списка задач")
    @ApiResponse(responseCode = "200", description = "Успешно")
    public TaskListResponseDTO createTaskList(
            @RequestBody TaskListRequestDTO taskListRequestDTO
    ) {
        return taskListService.createTaskList(taskListRequestDTO);
    }

    @GetMapping
    @Operation(summary = "Получение списков задач")
    @ApiResponse(responseCode = "200", description = "Успешно")
    public Page<TaskListResponseDTO> getAllTaskLists(
            @PageableDefault(page = 0, size = 10)
            @ParameterObject
            Pageable pageable
    ) {
        return taskListService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение списка задач по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    public TaskListResponseDTO getTaskListById(
            @Parameter(description = "ID списка задач")
            @PathVariable Long id
    ) {
        return taskListService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление списка задач по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    public TaskListResponseDTO updateTaskListById(
            @Parameter(description = "ID списка задач")
            @PathVariable Long id, @RequestBody @Valid TaskListRequestDTO taskListRequestDTO) {
        return taskListService.updateById(id, taskListRequestDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление списка задач по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    public TaskListResponseDTO patchTaskListById(
            @Parameter(description = "ID списка задач")
            @PathVariable Long id,
            @RequestBody PatchTaskListRequestDTO patchTaskListRequestDTO
    ) {
        return taskListService.patchById(id, patchTaskListRequestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление задачи по ID")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    public void deleteTaskListById(
            @Parameter(description = "ID списка задач")
            @PathVariable Long id
    ) {
        taskListService.deleteById(id);
    }

    @PostMapping("/{listId}/tasks/{taskId}")
    @Operation(summary = "Добавление существующей задачи в список задач")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public void addTaskToListByIds(
            @Parameter(description = "ID списка задач")
            @PathVariable Long listId,
            @Parameter(description = "ID задачи")
            @PathVariable Long taskId
    ) {
        taskListService.addTaskToList(listId, taskId);
    }

    @DeleteMapping("/{listId}/tasks/{taskId}")
    @Operation(summary = "Удаление задачи из списка задач")
    @ApiResponse(responseCode = "200", description = "Успешно")
    @ApiResponse(responseCode = "404", description = "Список задач не найден")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    public void removeTaskFromListByIds(
            @Parameter(description = "ID списка задач")
            @PathVariable Long listId,
            @Parameter(description = "ID задачи")
            @PathVariable Long taskId
    ) {
        taskListService.deleteTaskFromList(listId, taskId);
    }
}
