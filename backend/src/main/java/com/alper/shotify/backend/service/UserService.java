package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.UserEntity;
import com.alper.shotify.backend.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
