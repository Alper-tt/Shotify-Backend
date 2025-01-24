package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
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
    public ResponseEntity<String> createUser(@RequestBody UserEntity user){
        userService.create(user.getUsername(),user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Kullancı Oluşturuldu: " + user.getUsername());
    }

    @GetMapping
    @Operation(summary = "Kullanıcıları listele")
    public ResponseEntity<List<UserEntity>> getUsers(){
            List<UserEntity> users = userService.getUsers();
            return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı getir")
    public ResponseEntity<UserEntity> getUserById(@PathVariable int id){
            UserEntity user = userService.getUserById(id);
            return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı sil")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.ok("Kullanıcı silindi: "+id);
    }

    @PutMapping
    @Operation(summary = "Kullanıcı bilgilerini güncelle")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO){
            userService.update(updateUserRequestDTO);
            return ResponseEntity.ok("Kullanıcı güncellendi: "+updateUserRequestDTO.getUsername());
    }
}
