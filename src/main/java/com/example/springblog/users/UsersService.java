package com.example.springblog.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;
import com.example.springblog.users.dtos.CreateUserDTO;
import com.example.springblog.users.dtos.LoginUserDTO;
import com.example.springblog.users.dtos.UserResponseDTO;
import com.example.springblog.security.jwt.JWTService;
import com.example.springblog.security.authToken.AuthTokenService;

@Service
public class UsersService {
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthTokenService authTokenService;

    public UsersService(UsersRepository userRepository, 
                        ModelMapper modelMapper, 
                        PasswordEncoder passwordEncoder,
                        JWTService jwtService, 
                        AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.authTokenService = authTokenService;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {

        //TODO: Encrypt password: Done
        //TODO: Validate email
        //TODO: Check if username exists
        var newUser = modelMapper.map(createUserDTO, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        var savedUser = userRepository.save(newUser);
        var userResponseDTO = modelMapper.map(savedUser, UserResponseDTO.class);
        userResponseDTO.setToken(jwtService.createJWT(savedUser.getId()));
        return userResponseDTO;
    }

    public UserResponseDTO loginUser(LoginUserDTO loginUserDTO, AuthType authType) {
        var userEntity = userRepository.findByUsername(loginUserDTO.getUsername());
        if (userEntity == null) {
            throw new UserNotFoundException(loginUserDTO.getUsername());
        }
        //TODO: Check Encrypted password: Done
        var passwordMatch = passwordEncoder.matches(loginUserDTO.getPassword(), userEntity.getPassword());
        if (!passwordMatch) {
            throw new IllegalArgumentException("Incorrect Password!");
        }
        
        var userResponseDTO = modelMapper.map(userEntity, UserResponseDTO.class);
        switch (authType) {
            case JWT:
                userResponseDTO.setToken(jwtService.createJWT(userEntity.getId()));
                break;
            case Auth_Token: 
                userResponseDTO.setToken(authTokenService.createToken(userEntity).toString());
                break;
        }
        
        return userResponseDTO;
    }

    public static class UserNotFoundException extends IllegalArgumentException {
        public UserNotFoundException(String username) {
            super("User with username: " + username + " not found");
        }
        
        public UserNotFoundException(Long id) {
            super("User with user id: " + id + " not found");
        }
    }

    static enum AuthType {
        JWT,
        Auth_Token,
    }


}
