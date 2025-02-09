package com.alper.shotify.backend.model.response;

import lombok.Data;

import java.util.List;

@Data
public class SongRecommendationResponse {
    private List<SongResponseDTO> songs;
}
