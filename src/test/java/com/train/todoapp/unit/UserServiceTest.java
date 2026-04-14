package com.train.todoapp.unit;

import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.UserRequestDTO;
import com.train.todoapp.entity.dto.response.UserResponseDTO;
import com.train.todoapp.entity.mapper.UserMapper;
import com.train.todoapp.exception.UserNotFoundException;
import com.train.todoapp.repository.UserRepository;
import com.train.todoapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("UserService: unit tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Создание пользователя должно сохранять пользователя и возвращать DTO")
    @Test
    void shouldCreateUser() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setUsername("Alice");

        User user = new User();
        user.setUsername("Alice");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("Alice");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("Alice");

        when(userMapper.toUserEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("Alice");
        verify(userRepository).save(user);
    }

    @DisplayName("Получение пользователя по ID должно возвращать DTO")
    @Test
    void shouldGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Alice");

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsername("Alice");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("Alice");
    }

    @DisplayName("Получение несуществующего пользователя должно бросать исключение")
    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("1");
    }

    @DisplayName("Удаление пользователя должно вызывать репозиторий")
    @Test
    void shouldDeleteUserById() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository).delete(user);
    }
}
