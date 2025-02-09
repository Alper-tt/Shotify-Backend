package com.alper.shotify.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IntegrationResponse {
    private List<String> detectedKeywords;
    private List<RecommendedSongsListResponseDTO> recommendedSongs;
}
