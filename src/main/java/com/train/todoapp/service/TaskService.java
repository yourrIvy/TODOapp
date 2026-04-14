package com.train.todoapp.service;

import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.dto.request.PatchTaskRequestDTO;
import com.train.todoapp.entity.dto.request.TaskRequestDTO;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.entity.Task;
import com.train.todoapp.exception.TaskNotFoundException;
import com.train.todoapp.entity.mapper.TaskMapper;
import com.train.todoapp.exception.TaskNotInListException;
import com.train.todoapp.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        Task task = taskMapper.toTaskEntity(taskRequestDTO);
        task.setCreatedAt(LocalDateTime.now());
        return taskMapper.toTaskResponseDTO(taskRepository.save(task));
    }

    public Page<TaskResponseDTO> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::toTaskResponseDTO);
    }

    @Cacheable(value = "tasks", key = "#id")
    public TaskResponseDTO getById(Long id) {
        Task task = checkTaskExists(id);
        return taskMapper.toTaskResponseDTO(task);
    }

    @CachePut(value = "tasks", key = "#result.id")
    public TaskResponseDTO updateById(Long id, TaskRequestDTO taskRequestDTO) {
        Task task = checkTaskExists(id);

        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setCompleted(taskRequestDTO.getCompleted());

        return taskMapper.toTaskResponseDTO(taskRepository.save(task));
    }

    @CachePut(value = "tasks", key = "#result.id")
    public TaskResponseDTO patchTaskById(Long id, PatchTaskRequestDTO patchTaskRequestDTO) {
        Task task = checkTaskExists(id);

        taskMapper.patchEntity(patchTaskRequestDTO, task);

        return taskMapper.toTaskResponseDTO(taskRepository.save(task));
    }

    @CacheEvict(value = "tasks", key = "#id")
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @CachePut(value = "tasks", key = "#result.id")
    public TaskResponseDTO addTaskListId(Long taskId, TaskList taskList) {
        Task task = checkTaskExists(taskId);
        task.setTaskList(taskList);
        return taskMapper.toTaskResponseDTO(taskRepository.save(task));
    }

    @CachePut(value = "tasks", key = "result.id")
    public TaskResponseDTO removeTaskListId(Long taskId, Long listId) {
        Task task = checkTaskExists(taskId);

        if (task.getTaskList() == null || !task.getTaskList().getId().equals(listId)) {
            throw new TaskNotInListException(listId, taskId);
        }

        task.setTaskList(null);
        return taskMapper.toTaskResponseDTO(taskRepository.save(task));
    }

    private Task checkTaskExists(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
