package com.alper.shotify.backend.model.request;

import lombok.Data;

@Data
public class CreateVideoRequestDTO {
    private String photoPath;
    private String audioUrl;
}
