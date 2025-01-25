package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.model.request.CreateUserRequestDTO;
import com.alper.shotify.backend.model.response.UserResponseDTO;
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

    public UserResponseDTO create(CreateUserRequestDTO requestDTO) {
        if(userRepository.existsByUsername(requestDTO.getUsername()) || userRepository.existsByEmail(requestDTO.getEmail()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kullanıcı adı veya E-posta zaten mevcut");
        }
        UserEntity user = UserEntity.builder()
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .build();
        userRepository.save(user);
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public List<UserResponseDTO> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail()
                )).toList();
    }

    public UserResponseDTO getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    public void deleteById(int id) {
        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO update(UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity existUser = userRepository.findById(updateUserRequestDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        existUser.setUsername(updateUserRequestDTO.getUsername());
        existUser.setEmail(updateUserRequestDTO.getEmail());
        userRepository.save(existUser);
        return new UserResponseDTO(
                existUser.getUserId(),
                existUser.getUsername(),
                existUser.getEmail()
        );
    }
}
