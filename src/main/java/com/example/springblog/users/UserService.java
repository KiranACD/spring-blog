package com.example.springblog.users;

import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import com.example.springblog.users.dtos.CreateUserDTO;
import com.example.springblog.users.dtos.LoginUserDTO;
import com.example.springblog.users.dtos.UserResponseDTO;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {

        //TODO: Encrypt password
        //TODO: Validate email
        //TODO: Check if username exists
        var newUser = modelMapper.map(createUserDTO, UserEntity.class);
        var savedUser = userRepository.save(newUser);
        var userResponseDTO = modelMapper.map(savedUser, UserResponseDTO.class);
        
        return userResponseDTO;
    }

    public UserResponseDTO loginUser(LoginUserDTO loginUserDTO) {
        var userEntity = userRepository.findByUsername(loginUserDTO.getUsername());
        if (userEntity == null) {
            throw new UserNotFoundException(loginUserDTO.getUsername());
        }
        //TODO: Check Encrypted password
        if (!userEntity.getPassword().equals(loginUserDTO.getPassword())) {
            throw new IllegalArgumentException("Incorrect Password!");
        }
        var userResponseDTO = modelMapper.map(userEntity, UserResponseDTO.class);
        return userResponseDTO;
    }

    public static class UserNotFoundException extends IllegalArgumentException {
        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        } 
    }


}
