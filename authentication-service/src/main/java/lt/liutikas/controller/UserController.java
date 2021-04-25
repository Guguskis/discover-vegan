package lt.liutikas.controller;

import lt.liutikas.dto.CreateUserRequestDto;
import lt.liutikas.dto.CreateUserResponseDto;
import lt.liutikas.dto.LoginRequestDto;
import lt.liutikas.dto.LoginResponseDto;
import lt.liutikas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @PostMapping
    private ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.ok(userService.createUser(createUserRequestDto));
    }

}
