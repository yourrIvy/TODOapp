package com.train.todoapp.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchTaskListRequestDTO {

    @Size(max = 255)
    @Schema(description = "Имя списка задач", example = "Планы на отпуск")
    private String name;

    @Schema(description = "ID инициатора", example = "1")
    private Long authorId;

    @Schema(description = "ID исполнителя", example = "2")
    private Long executorId;
}