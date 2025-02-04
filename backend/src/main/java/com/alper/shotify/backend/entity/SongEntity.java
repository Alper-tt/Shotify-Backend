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

    @Column(name = "Title")
    private String songTitle;

    @Column(name = "Artist")
    private String songArtist;

    @Column(name = "URL")
    private String songUrl;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    private List<RecommendationEntity> recommendations;
}
