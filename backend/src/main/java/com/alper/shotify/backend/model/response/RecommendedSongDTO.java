package com.alper.shotify.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedSongDTO {
    private String songArtist;
    private int songId;
    private String songTitle;
}
