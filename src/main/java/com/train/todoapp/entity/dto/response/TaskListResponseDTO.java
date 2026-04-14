package com.train.todoapp.entity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TaskListResponseDTO {

    @Schema(description = "ID списка задач", example = "1")
    private Long id;

    @Schema(description = "Имя списка задач", example = "Планы на отпуск")
    private String name;

    @Schema(description = "ID инициатора", example = "1")
    private Long authorId;

    @Schema(description = "ID исполнителя", example = "2")
    private Long executorId;

    @Schema(description = "Задачи из списка")
    List<TaskResponseDTO> tasks;
}
