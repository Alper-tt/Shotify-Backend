package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.model.request.CreateUserRequestDTO;
import com.alper.shotify.backend.model.response.UserResponseDTO;
import com.alper.shotify.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "User işlemleri için API'ler")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Kullanıcı oluştur")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(requestDTO));
    }

    @GetMapping
    @Operation(summary = "Kullanıcıları listele")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
            return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı getir")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id){
            return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı sil")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.ok("Kullanıcı silindi: " + id);
    }

    @PutMapping
    @Operation(summary = "Kullanıcı bilgilerini güncelle")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO){
            return ResponseEntity.ok(userService.update(updateUserRequestDTO));
    }
}
