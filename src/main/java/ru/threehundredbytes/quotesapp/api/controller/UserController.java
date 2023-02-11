package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.request.UserRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.UserResponseDTO;
import ru.threehundredbytes.quotesapp.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody UserRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }
}
