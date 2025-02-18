package com.alper.shotify.backend.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectDetectionResponse {
    private List<String> keywords;
}
