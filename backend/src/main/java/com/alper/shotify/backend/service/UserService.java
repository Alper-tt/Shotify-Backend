package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public void create(String username, String email) {
        if(userRepository.existsByUsername(username) || userRepository.existsByEmail(email))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kullanıcı adı veya E-posta zaten mevcut");
        }
        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .build();
        userRepository.save(user);
    }

    public List<UserEntity> getUsers(){
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return users;
    }

    public UserEntity getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
    }

    public void deleteById(int id) {
        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı");
        }
        userRepository.deleteById(id);
    }

    public void update(UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity existUser = userRepository.findById(updateUserRequestDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        existUser.setUsername(updateUserRequestDTO.getUsername());
        existUser.setEmail(updateUserRequestDTO.getEmail());
        userRepository.save(existUser);
    }
}
