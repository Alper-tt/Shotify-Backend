package com.alper.shotify.backend.entity;

import com.alper.shotify.backend.model.response.RecommendedSongsListResponseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private int recommendationId;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private PhotoEntity photo;

    @ManyToMany
    @JoinTable(
            name = "recommendation_songs",
            joinColumns = @JoinColumn(name = "recommendation_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<SongEntity> songs;
}
