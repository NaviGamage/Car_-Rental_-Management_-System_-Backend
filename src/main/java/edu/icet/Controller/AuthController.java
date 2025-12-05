package edu.icet.Controller;

import edu.icet.Model.Dto.LoginRequestDto;
import edu.icet.Model.Dto.RegistrationRequestDto;
import edu.icet.Model.Dto.UserResponseDto;
import edu.icet.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@Valid @RequestBody RegistrationRequestDto req){
        try {
            return  userService.register(req);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto req){
        return userService.login(req);
    }

}
