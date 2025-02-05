package com.alper.shotify.backend.model.response;

import com.alper.shotify.backend.entity.RecommendationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponseDTO {
    private int photoId;
    private int userId;
    private String url;
    private String analysisData;
    private RecommendationEntity recommendation;
}
