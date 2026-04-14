package com.train.todoapp.unit;

import com.train.todoapp.entity.Task;
import com.train.todoapp.entity.dto.request.TaskRequestDTO;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.entity.mapper.TaskMapper;
import com.train.todoapp.exception.TaskNotFoundException;
import com.train.todoapp.repository.TaskRepository;
import com.train.todoapp.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("TaskService: unit tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Создание задачи должно сохранять дату создания и возвращать DTO")
    void shouldCreateTask() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Test");

        Task task = Task.builder().title("Test").build();
        Task savedTask = Task.builder().id(1L).title("Test").createdAt(LocalDateTime.now()).build();

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(1L);

        when(taskMapper.toTaskEntity(dto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.toTaskResponseDTO(savedTask)).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.createTask(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(task.getCreatedAt()).isNotNull();
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Получение задачи по ID должно возвращать DTO")
    void shouldGetTaskById() {
        Task task = Task.builder().id(1L).title("Test").build();
        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(responseDTO);

        TaskResponseDTO result = taskService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Получение несуществующей задачи должно бросать исключение")
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getById(1L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Удаление задачи должно вызывать репозиторий")
    void shouldDeleteTaskById() {
        taskService.deleteById(1L);

        verify(taskRepository).deleteById(1L);
    }
}
