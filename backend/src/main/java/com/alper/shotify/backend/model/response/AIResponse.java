package com.alper.shotify.backend.model.response;

import com.alper.shotify.backend.model.request.AIRequest.Message;
import lombok.Data;

import java.util.List;

@Data
public class AIResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;

        @Data
        public static class Message {
            private String content;
        }

    }
}
