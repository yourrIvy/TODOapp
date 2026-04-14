package com.train.todoapp.entity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Username must not be blank")
    @Size(max = 255)
    @Schema(description = "Имя пользователя", example = "ivan")
    private String username;
}
