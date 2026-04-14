package com.train.todoapp.service;

import com.train.todoapp.entity.Task;
import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.PatchTaskListRequestDTO;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.entity.mapper.TaskListMapper;
import com.train.todoapp.exception.TaskNotFoundException;
import com.train.todoapp.repository.TaskListRepository;

import com.train.todoapp.repository.TaskRepository;
import com.train.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final TaskListMapper taskListMapper;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskListResponseDTO createTaskList(TaskListRequestDTO taskListRequestDTO) {
        TaskList taskList = taskListMapper.toTaskListEntity(taskListRequestDTO);
        taskList.getTasks().forEach(task -> task.setTaskList(taskList));

        return taskListMapper.toTaskListResponseDTO(taskListRepository.save(taskList));
    }

    public Page<TaskListResponseDTO> getAll(Pageable pageable) {
        return taskListRepository.findAll(pageable)
                .map(taskListMapper::toTaskListResponseDTO);
    }

    @Cacheable(value = "task_lists", key = "#id")
    public TaskListResponseDTO getById(Long id) {
        TaskList taskList = taskListRepository.findByIdWithTasks(id)
                .orElseThrow(() -> new RuntimeException("TaskList not found"));
        return taskListMapper.toTaskListResponseDTO(taskList);
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO  updateById(Long id, TaskListRequestDTO taskListRequestDTO) {
        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskList not found"));

        User author = userRepository.findById(taskListRequestDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        User executor = userRepository.findById(taskListRequestDTO.getExecutorId())
                .orElseThrow(() -> new RuntimeException("Executor not found"));

        taskList.setName(taskListRequestDTO.getName());
        taskList.setAuthor(author);
        taskList.setExecutor(executor);

        return taskListMapper.toTaskListResponseDTO(taskListRepository.save(taskList));
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO patchById(Long id, PatchTaskListRequestDTO patchTaskListRequestDTO) {
        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskList not found"));

        userRepository.findById(patchTaskListRequestDTO.getAuthorId()).orElseThrow(() -> new RuntimeException("Author not found"));
        userRepository.findById(patchTaskListRequestDTO.getExecutorId()).orElseThrow(() -> new RuntimeException("Executor not found"));

        taskListMapper.patchEntity(patchTaskListRequestDTO, taskList);

        return taskListMapper.toTaskListResponseDTO(taskListRepository.save(taskList));
    }

    @CacheEvict(value = "task_lists", key = "#id")
    public void deleteById(Long id) {
        taskListRepository.deleteById(id);
    }

    public void addTaskToList(Long listId, Long taskId) {
        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("TaskList not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        task.setTaskList(taskList);
        taskRepository.save(task);
    }

    public void deleteTaskFromList(Long listId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (task.getTaskList() == null || !task.getTaskList().getId().equals(listId)) {
            throw new RuntimeException("Task not found in this list");
        }

        task.setTaskList(null);
        taskRepository.save(task);
    }
}
