package com.train.todoapp.entity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponseDTO {

    @Schema(description = "ID задачи", example = "1")
    private Long id;

    @Schema(description = "Заголовок задачи", example = "Купить батон")
    private String title;

    @Schema(description = "Описание", example = "Идём в магазин, покупаем батон белого хлеба, приносим домой")
    private String description;

    @Schema(description = "Флаг выполнения", example = "false")
    private Boolean completed;

    @Schema(description = "ДатаВремя создания задачи в системе", example = "2026-05-18T11:59:59.0001")
    private LocalDateTime createdAt;

    @Schema(description = "Дедлайн", example = "2026-05-18T11:59:59.0001")
    private LocalDateTime dueDate;

    @Schema(description = "ID списка задач", example = "1")
    private Long taskListId;
}
