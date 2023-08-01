package com.example.springblog.users;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springblog.users.dtos.CreateUserDTO;
import com.example.springblog.users.dtos.LoginUserDTO;
import com.example.springblog.users.dtos.UserResponseDTO;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService userService;

    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var savedUser = userService.createUser(createUserDTO);
        return ResponseEntity
            .created(URI.create("/users/" + savedUser.getId()))
            .body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(
                            @RequestBody LoginUserDTO loginUserDTO,
                            @RequestParam(name="token", defaultValue ="jwt") String token) {
        
        var authType = UsersService.AuthType.JWT;
        if (token.equals("auth_token")) {
            authType = UsersService.AuthType.Auth_Token;
        }
        var userEntity = userService.loginUser(loginUserDTO, authType);
        return ResponseEntity.ok(userEntity);
    }

    @ExceptionHandler(UsersService.UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UsersService.UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
