package com.example.springblog.users;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.springblog.users.dtos.CreateUserDTO;

@DataJpaTest
public class UserServiceTests {
    private UserService userService;

        @Autowired
        private UserRepository userRepository;

        private UserService createUserService() {
            return new UserService(
                userRepository,
                new ModelMapper()
            );
        }

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
