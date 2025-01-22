package com.alper.shotify.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recommendations")
public class RecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @Column(name = "Photo ID")
    private Long photoId;

    @Column(name = "Song ID")
    private Long songId;
}
