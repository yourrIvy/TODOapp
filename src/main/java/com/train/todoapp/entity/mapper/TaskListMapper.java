package com.train.todoapp.entity.mapper;

import com.train.todoapp.entity.TaskList;
import com.train.todoapp.entity.dto.request.PatchTaskListRequestDTO;
import com.train.todoapp.entity.dto.request.TaskListRequestDTO;
import com.train.todoapp.entity.dto.response.TaskListResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {

    @Mapping(target = "author.id", source = "authorId")
    @Mapping(target = "executor.id", source = "executorId")
    @Mapping(target = "tasks", ignore = true)
    TaskList toTaskListEntity(TaskListRequestDTO requestDTO);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "executorId", source = "executor.id")
    @Mapping(target = "tasks", source = "tasks")
    TaskListResponseDTO toTaskListResponseDTO(TaskList entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(PatchTaskListRequestDTO patchTaskListRequestDTO, @MappingTarget TaskList entity);
}
