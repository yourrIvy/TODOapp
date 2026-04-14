package com.train.todoapp.exception;

public class TaskNotInListException extends RuntimeException {
    public TaskNotInListException(Long listId, Long taskId) {super("Task with id " + taskId + " not found in list with id " + listId);
    }
}
