package com.alper.shotify.backend.model.request;

import com.alper.shotify.backend.model.response.RecommendedSongsListResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecommendationRequestDTO {
    private int photoId;
    private RecommendedSongsListResponseDTO recommendedSongs;
}
