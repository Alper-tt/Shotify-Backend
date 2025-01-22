package com.alper.shotify.backend.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Long songId;

    @Column(name = "Title")
    private String songTitle;

    @Column(name = "Artist")
    private String songArtist;

    @Column(name = "URL")
    private String songUrl;

    @Column(name = "Lyrics")
    private String lyrics;
}
