package com.train.todoapp.integration;

import com.train.todoapp.entity.User;
import com.train.todoapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("UserRepository: работа с БД")
class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Сохраняет пользователя в БД")
    @Test
    void shouldSaveUser() {
        User user = new User();
        user.setUsername("Charlie");

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("Charlie");
    }

    @DisplayName("Находит пользователя по идентификатору")
    @Test
    @Sql(scripts = "/sql/insert-users.sql")
    void shouldFindUserById() {
        User user = userRepository.findById(1L).orElseThrow();

        assertThat(user.getUsername()).isEqualTo("Alice");
    }
}
