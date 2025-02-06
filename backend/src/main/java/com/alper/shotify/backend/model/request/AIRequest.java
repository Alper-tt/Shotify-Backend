package com.alper.shotify.backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class AIRequest {
    private String model = "deepseek/deepseek-r1:free";
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
