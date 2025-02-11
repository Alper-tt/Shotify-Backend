package com.alper.shotify.backend.model.response;

import com.alper.shotify.backend.entity.SongEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDTO {
    private int recommendationId;
    private int photoId;
    private List<SongResponseDTO> songs;
}
