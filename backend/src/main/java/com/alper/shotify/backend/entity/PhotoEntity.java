package com.alper.shotify.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "photos")
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(name = "Photo URL")
    private String url;

    @Column(name = "Userd ID")
    private Long userId;

    @Column(name = "Analysis Data")
    private String analysisData;
}
