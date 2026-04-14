package com.train.todoapp.integration;

import com.train.todoapp.entity.Task;
import com.train.todoapp.repository.TaskRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("TaskRepository: работа с БД")
class TaskRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Сохраняем задачу в БД")
    void shouldSaveTask() {
        Task task = Task.builder()
                .title("Integration test")
                .createdAt(LocalDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);

        assertThat(savedTask.getId()).isNotNull();
    }

    @Test
    @DisplayName("Находим задачу по ID")
    @Sql(scripts = {"/sql/insert-users.sql",
                    "/sql/insert-task-lists.sql",
                    "/sql/insert-task.sql"})
    void shouldFindTaskById() {
        Task task = taskRepository.findById(1L).orElseThrow();

        assertThat(task.getTitle()).isEqualTo("Task 1");
    }
}
