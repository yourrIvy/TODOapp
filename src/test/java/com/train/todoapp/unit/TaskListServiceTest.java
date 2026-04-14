package com.train.todoapp.unit;

import com.train.todoapp.entity.Task;
import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.entity.mapper.TaskListMapper;
import com.train.todoapp.exception.TaskNotInListException;
import com.train.todoapp.repository.TaskListRepository;
import com.train.todoapp.service.TaskListService;
import com.train.todoapp.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("TaskListService: unit tests")
class TaskListServiceTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private TaskListMapper taskListMapper;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskListService taskListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Создание списка должно установить taskList для задач и сохранить сущность")
    void shouldCreateTaskListWithTasks() {
        TaskListRequestDTO request = new TaskListRequestDTO();
        request.setName("List 1");
        request.setAuthorId(1L);
        request.setExecutorId(2L);

        TaskList taskList = new TaskList();
        taskList.setName("List 1");
        taskList.setAuthor(new User());
        taskList.setExecutor(new User());
        Task task = Task.builder().title("Task X").build();
        taskList.setTasks(List.of(task));

        TaskListResponseDTO response = new TaskListResponseDTO();
        response.setId(1L);

        when(taskListMapper.toTaskListEntity(request)).thenReturn(taskList);
        when(taskListRepository.save(taskList)).thenReturn(taskList);
        when(taskListMapper.toTaskListResponseDTO(taskList)).thenReturn(response);

        TaskListResponseDTO result = taskListService.createTaskList(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(taskList.getTasks()).allSatisfy(savedTask -> assertThat(savedTask.getTaskList()).isEqualTo(taskList));
        verify(taskListRepository).save(taskList);
    }

    @Test
    @DisplayName("Добавление задачи в список должно сохранить задачу")
    void shouldAddTaskToListAndSaveTask() {
        TaskList taskList = new TaskList();
        taskList.setId(11L);

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(21L);

        TaskListResponseDTO response = new TaskListResponseDTO();
        response.setId(11L);

        when(taskListRepository.findById(11L)).thenReturn(Optional.of(taskList));
        when(taskService.addTaskListId(21L, taskList)).thenReturn(taskResponseDTO);
        when(taskListRepository.findById(11L)).thenReturn(Optional.of(taskList));
        when(taskListMapper.toTaskListResponseDTO(taskList)).thenReturn(response);

        TaskListResponseDTO result = taskListService.addTaskToList(11L, 21L);

        assertThat(result.getId()).isEqualTo(11L);
        verify(taskService).addTaskListId(21L, taskList);
    }

    @Test
    @DisplayName("Удаление задачи из списка должно очищать связь и сохранять задачу")
    void shouldRemoveTaskFromListAndSaveTask() {
        TaskList taskList = new TaskList();
        taskList.setId(12L);

        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(22L);

        TaskListResponseDTO response = new TaskListResponseDTO();
        response.setId(12L);

        when(taskListRepository.findById(12L)).thenReturn(Optional.of(taskList));
        when(taskService.removeTaskListId(22L, 12L)).thenReturn(taskResponseDTO);
        when(taskListRepository.findById(12L)).thenReturn(Optional.of(taskList));
        when(taskListMapper.toTaskListResponseDTO(taskList)).thenReturn(response);

        TaskListResponseDTO result = taskListService.deleteTaskFromList(12L, 22L);

        assertThat(result.getId()).isEqualTo(12L);
        verify(taskService).removeTaskListId(22L, 12L);
    }

    @Test
    @DisplayName("Удаление задачи из другого списка должно бросать исключение")
    void shouldThrowWhenRemovingTaskFromAnotherList() {
        TaskList taskList = new TaskList();
        taskList.setId(13L);

        when(taskListRepository.findById(13L)).thenReturn(Optional.of(taskList));
        when(taskService.removeTaskListId(23L, 13L)).thenThrow(new TaskNotInListException(13L, 23L));

        assertThatThrownBy(() -> taskListService.deleteTaskFromList(13L, 23L))
                .isInstanceOf(TaskNotInListException.class)
                .hasMessageContaining("Task with id 23 not found in list with id 13");

        verify(taskService).removeTaskListId(23L, 13L);
    }
}
