package com.train.todoapp.entity.mapper;

import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.UserRequestDTO;
import com.train.todoapp.entity.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);

    User toUserEntity(UserRequestDTO userRequestDTO);
}
