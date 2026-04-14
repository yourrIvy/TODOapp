package com.train.todoapp.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}
