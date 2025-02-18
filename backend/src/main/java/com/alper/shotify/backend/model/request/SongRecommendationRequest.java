package com.alper.shotify.backend.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SongRecommendationRequest {
    private List<String> keywords;
}
