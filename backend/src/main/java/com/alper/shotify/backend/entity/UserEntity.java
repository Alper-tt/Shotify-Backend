package com.alper.shotify.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "Email")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<PhotoEntity> photos;
}
