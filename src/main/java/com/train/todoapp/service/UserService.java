package com.train.todoapp.service;

import com.train.todoapp.entity.User;
import com.train.todoapp.entity.dto.request.UserRequestDTO;
import com.train.todoapp.entity.dto.response.UserResponseDTO;
import com.train.todoapp.entity.mapper.UserMapper;
import com.train.todoapp.exception.UserNotFoundException;
import com.train.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toUserEntity(userRequestDTO);
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO getById(Long id) {
        User user = checkUserExists(id);
        return userMapper.toUserResponseDTO(user);
    }

    public void deleteById(Long id) {
        User user = checkUserExists(id);
        userRepository.delete(user);
    }

    private User checkUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
