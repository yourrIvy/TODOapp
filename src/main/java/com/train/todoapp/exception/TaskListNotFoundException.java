package com.train.todoapp.exception;

public class TaskListNotFoundException extends RuntimeException {
    public TaskListNotFoundException(Long id) {
        super("TaskList with id " + id + " not found");
    }
}
