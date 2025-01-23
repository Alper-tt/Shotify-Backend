package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<UserEntity>> getUsers(){
        try {
            List<UserEntity> users = userService.getUsers();
            return ResponseEntity.ok(users);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id){
        try {
            UserEntity user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Kullanıcı silindi: "+id);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO){
        try {
            userService.update(updateUserRequestDTO);
            return ResponseEntity.ok("Kullanıcı güncellendi: "+updateUserRequestDTO.getUsername());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
