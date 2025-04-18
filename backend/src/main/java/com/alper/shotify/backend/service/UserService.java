package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.model.request.UpdateUserRequestDTO;
import com.alper.shotify.backend.model.request.CreateUserRequestDTO;
import com.alper.shotify.backend.model.response.PhotoResponseDTO;
import com.alper.shotify.backend.model.response.UserResponseDTO;
import com.alper.shotify.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public UserResponseDTO getOrCreateUser(CreateUserRequestDTO requestDTO) {
        if(userRepository.existsByFirebaseUid(requestDTO.getFirebaseUid()))
        {
            UserEntity user = userRepository.findByFirebaseUid(requestDTO.getFirebaseUid());
            return new UserResponseDTO(
                    user.getUserId(),
                    user.getFirebaseUid(),
                    user.getEmail()
            );
        }else {
            UserEntity user = UserEntity.builder()
                    .firebaseUid(requestDTO.getFirebaseUid())
                    .email(requestDTO.getEmail())
                    .build();
            userRepository.save(user);
            return new UserResponseDTO(
                    user.getUserId(),
                    user.getFirebaseUid(),
                    user.getEmail()
            );
        }
    }

    public List<UserResponseDTO> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getUserId(),
                        user.getFirebaseUid(),
                        user.getEmail()
                )).toList();
    }

    public UserResponseDTO getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));
        return new UserResponseDTO(
                user.getUserId(),
                user.getFirebaseUid(),
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
        existUser.setFirebaseUid(updateUserRequestDTO.getFirebaseUid());
        existUser.setEmail(updateUserRequestDTO.getEmail());
        userRepository.save(existUser);
        return new UserResponseDTO(
                existUser.getUserId(),
                existUser.getFirebaseUid(),
                existUser.getEmail()
        );
    }

    @Transactional
    @Cacheable(value = "photos", key = "#userId")
    public List<PhotoResponseDTO> getUserPhotos(int userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı"));

        return user.getPhotos().stream().map(
                photo -> new PhotoResponseDTO(
                        photo.getPhotoId(),
                        user.getUserId(),
                        photo.getPhotoPath(),
                        photo.getUrl()
                )
        ).toList();
    }
}
