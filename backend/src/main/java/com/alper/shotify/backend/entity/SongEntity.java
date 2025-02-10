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
@Table(name = "songs")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songId;

    @Column(name = "songTitle")
    private String songTitle;

    @Column(name = "songArtist")
    private String songArtist;

    @Column(name = "songAlbum")
    private String songAlbum;

    @Column(name = "songlyrics", columnDefinition = "TEXT")
    private String songLyrics;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    private List<RecommendationEntity> recommendations;
}
