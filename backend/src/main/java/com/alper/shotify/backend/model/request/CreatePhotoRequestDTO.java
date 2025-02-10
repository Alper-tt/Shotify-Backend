package com.alper.shotify.backend.model.request;

import com.alper.shotify.backend.entity.RecommendationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePhotoRequestDTO {
    private String url;
    private int userId;
    private String analysisData;
    private RecommendationEntity recommendation;
    private String photoPath;
}
