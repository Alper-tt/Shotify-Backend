package com.alper.shotify.backend.model.request;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.model.response.RecommendedSongsListResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecommendationRequestDTO {
    private int recommendationId;
    private int photoId;
    private RecommendedSongsListResponseDTO recommendedSongs;
}
