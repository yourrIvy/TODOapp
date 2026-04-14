package com.train.todoapp.unit;

import com.train.todoapp.entity.Task;
import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.entity.mapper.TaskListMapper;
import com.train.todoapp.repository.TaskListRepository;
import com.train.todoapp.repository.TaskRepository;
import com.train.todoapp.repository.UserRepository;
import com.train.todoapp.service.TaskListService;

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
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

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
    void shouldAddTaskToListAndSave() {
        TaskList taskList = new TaskList();
        taskList.setId(1L);

        Task task = Task.builder().id(2L).title("From add").build();

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskListService.addTaskToList(1L, 2L);

        assertThat(task.getTaskList()).isEqualTo(taskList);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Удаление задачи из списка должно очищать связь и сохранять задачу")
    void shouldRemoveTaskFromListAndSave() {
        TaskList taskList = new TaskList();
        taskList.setId(1L);

        Task task = Task.builder().id(2L).title("To remove").build();
        task.setTaskList(taskList);

        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskListService.deleteTaskFromList(1L, 2L);

        assertThat(task.getTaskList()).isNull();
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Удаление задачи из другого списка должно бросать исключение")
    void shouldThrowWhenRemovingTaskFromWrongList() {
        TaskList taskList = new TaskList();
        taskList.setId(1L);

        TaskList anotherList = new TaskList();
        anotherList.setId(2L);

        Task task = Task.builder().id(2L).title("Wrong list").build();
        task.setTaskList(anotherList);

        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskListService.deleteTaskFromList(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found in this list");

        verify(taskRepository, never()).save(any());
    }
}
