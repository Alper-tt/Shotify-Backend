package com.alper.shotify.backend.model.request;

import com.alper.shotify.backend.entity.RecommendationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSongRequestDTO {
    private int songId;
    private String songTitle;
    private String songArtist;
    private String songUrl;
    private String lyrics;
    private List<RecommendationEntity> recommendations;
}
