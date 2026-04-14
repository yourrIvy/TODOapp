package com.train.todoapp.service;

import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.PatchTaskListRequestDTO;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import com.train.todoapp.entity.mapper.TaskListMapper;
import com.train.todoapp.exception.TaskListNotFoundException;
import com.train.todoapp.exception.UserNotFoundException;
import com.train.todoapp.repository.TaskListRepository;

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
    private final TaskService taskService;

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
        TaskList taskList = checkTaskListExists(id);
        return taskListMapper.toTaskListResponseDTO(taskList);
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO  updateById(Long id, TaskListRequestDTO taskListRequestDTO) {
        TaskList taskList = checkTaskListExists(id);

        User author = checkUserExists(taskListRequestDTO.getAuthorId());
        User executor = checkUserExists(taskListRequestDTO.getExecutorId());

        taskList.setName(taskListRequestDTO.getName());
        taskList.setAuthor(author);
        taskList.setExecutor(executor);

        return taskListMapper.toTaskListResponseDTO(taskListRepository.save(taskList));
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO patchById(Long id, PatchTaskListRequestDTO patchTaskListRequestDTO) {
        TaskList taskList = checkTaskListExists(id);

        if (patchTaskListRequestDTO.getAuthorId() != null) {checkUserExists(patchTaskListRequestDTO.getAuthorId());}
        if (patchTaskListRequestDTO.getExecutorId() != null) {checkUserExists(patchTaskListRequestDTO.getExecutorId());}

        taskListMapper.patchEntity(patchTaskListRequestDTO, taskList);

        return taskListMapper.toTaskListResponseDTO(taskListRepository.save(taskList));
    }

    @CacheEvict(value = "task_lists", key = "#id")
    public void deleteById(Long id) {
        taskListRepository.deleteById(id);
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO addTaskToList(Long listId, Long taskId) {
        TaskList taskList = checkTaskListExists(listId);

        taskService.addTaskListId(taskId, taskList);
        return taskListMapper.toTaskListResponseDTO(checkTaskListExists(listId));
    }

    @CachePut(value = "task_lists", key = "#result.id")
    public TaskListResponseDTO deleteTaskFromList(Long listId, Long taskId) {
        checkTaskListExists(listId);

        taskService.removeTaskListId(taskId, listId);
        return taskListMapper.toTaskListResponseDTO(checkTaskListExists(listId));
    }

    private TaskList checkTaskListExists(Long id) {
        return taskListRepository.findById(id)
                .orElseThrow(() -> new TaskListNotFoundException(id));
    }

    private User checkUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
