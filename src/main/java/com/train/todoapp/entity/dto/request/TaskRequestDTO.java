package com.train.todoapp.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDTO {

    @NotBlank(message="Title must not be blank")
    @Size(max=255)
    @Schema(description = "Заголовок задачи", example = "Купить батон")
    private String title;

    @Size(max=255)
    @Schema(description = "Описание", example = "Идём в магазин, покупаем батон белого хлеба, приносим домой")
    private String description;

    @Schema(description = "Флаг выполнения", example = "false")
    private Boolean completed;

    @FutureOrPresent(message = "Due date must be in the future")
    @Schema(description = "Дедлайн", example = "2026-05-18T11:59:59.0001")
    private LocalDate dueDate;
}