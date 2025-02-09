package com.alper.shotify.backend.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ObjectDetectionResponse {
    private List<String> keywords;
}
