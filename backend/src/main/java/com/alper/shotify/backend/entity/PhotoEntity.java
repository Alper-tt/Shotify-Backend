package com.alper.shotify.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private int photoId;

    @Column(name = "Photo URL")
    private String url;

    @Column(name = "Analysis Data")
    private String analysisData;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @OneToOne(mappedBy = "photo", cascade = CascadeType.ALL)
    @JsonIgnore
    private RecommendationEntity recommendation;
}
