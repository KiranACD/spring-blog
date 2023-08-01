package com.example.springblog.users;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.springblog.security.jwt.JWTService;
import com.example.springblog.users.dtos.CreateUserDTO;

@DataJpaTest
public class UserServiceTests {
    private UsersService userService;

    @Autowired
    private UsersRepository userRepository;

    private UsersService createUserService() {
        if (userService == null) {
            var passwordEncoder = new BCryptPasswordEncoder();
            var jwtService = new JWTService();
            userService = new UsersService(
                userRepository,
                new ModelMapper(),
                passwordEncoder,
                jwtService
            );
        }
        return userService;
    }

    @Test   
    public void testCreateUser() {
        var newUserDTO = new CreateUserDTO();
        newUserDTO.setEmail("kiran.ram@gmail.com");
        newUserDTO.setUsername("KiranACD");
        newUserDTO.setPassword("xx");
        userService = createUserService();
        var savedUser = userService.createUser(newUserDTO);
        assertNotNull(savedUser);
    }

}
