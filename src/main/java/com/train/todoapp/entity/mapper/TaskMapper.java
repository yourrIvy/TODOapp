package com.train.todoapp.entity.mapper;

import com.train.todoapp.entity.dto.request.PatchTaskRequestDTO;
import com.train.todoapp.entity.dto.request.TaskRequestDTO;
import com.train.todoapp.entity.dto.response.TaskResponseDTO;
import com.train.todoapp.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "taskListId", source = "taskList.id")
    TaskResponseDTO toTaskResponseDTO(Task task);

    Task toTaskEntity(TaskRequestDTO taskRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(PatchTaskRequestDTO patchTaskRequestDTO, @MappingTarget Task task);
}
