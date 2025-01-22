package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserEntity user){
        userService.create(user.getUsername(),user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getUsername());
    }
}
