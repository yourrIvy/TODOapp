package com.train.todoapp.integration;

import com.train.todoapp.entity.TaskList;
import com.train.todoapp.repository.TaskListRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/insert-users.sql",
                "/sql/insert-task-lists.sql",
                "/sql/insert-task.sql"
})
@DisplayName("TaskListRepository: работа с БД")
class TaskListRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TaskListRepository taskListRepository;

    @Test
    @DisplayName("Находит список задач вместе с его задачами")
    void shouldFindTaskListWithTasks() {
        Optional<TaskList> optionalTaskList = taskListRepository.findById(1L);

        assertThat(optionalTaskList).isPresent();

        TaskList taskList = optionalTaskList.get();

        assertThat(taskList.getId()).isEqualTo(1L);
        assertThat(taskList.getName()).isEqualTo("Main List");

        assertThat(taskList.getTasks())
                .isNotNull()
                .hasSize(2);

        assertThat(taskList.getTasks())
                .extracting("title")
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }
}
