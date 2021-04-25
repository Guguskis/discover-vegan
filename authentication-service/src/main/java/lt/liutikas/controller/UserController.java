package lt.liutikas.controller;

import lt.liutikas.dto.CreateUserRequestDto;
import lt.liutikas.dto.CreateUserResponseDto;
import lt.liutikas.dto.GetTokenDto;
import lt.liutikas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/token")
    private ResponseEntity<GetTokenDto> getToken(@RequestParam String email,
                                                 @RequestParam String password) {
        return ResponseEntity.ok(userService.getToken(email, password));
    }

    @PostMapping
    private ResponseEntity<CreateUserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.ok(userService.createUser(createUserRequestDto));
    }

}
