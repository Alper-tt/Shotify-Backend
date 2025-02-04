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
public class CreateSongRequestDTO {
    private String songTitle;
    private String songArtist;
    private String songUrl;
    private List<RecommendationEntity> recommendations;
}
