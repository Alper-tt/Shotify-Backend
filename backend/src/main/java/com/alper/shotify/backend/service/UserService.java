package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public void create(String username, String email) {
        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .build();
        userRepository.save(user);
    }

    public List<UserEntity> getUsers(){
        if(userRepository.findAll().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı");
        }
        return userRepository.findAll();
    }

    public UserEntity getUserById(int id) {
        if(userRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı");
        }
        return userRepository.findById(id).get();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public void update(UpdateUserRequestDTO updateUserRequestDTO) {
        Optional<UserEntity> optionalUser = userRepository.findById(updateUserRequestDTO.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Kullanıcı bulunamadı");
        }
        UserEntity existUser = optionalUser.get();
        existUser.setUsername(updateUserRequestDTO.getUsername());
        existUser.setEmail(updateUserRequestDTO.getEmail());
        userRepository.save(existUser);
    }
}
