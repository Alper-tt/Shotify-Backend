package com.alper.shotify.backend.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationResponse {
    private List<String> detectedKeywords;
    private List<SongResponseDTO> recommendedSongs;
}
