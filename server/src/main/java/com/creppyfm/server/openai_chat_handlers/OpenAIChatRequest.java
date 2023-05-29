package com.creppyfm.server.openai_chat_handlers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OpenAIChatRequest {
    private String model;
    private List<ChatMessage> messages;

    public OpenAIChatRequest(String model, List<ChatMessage> messages, int max_tokens, int temperature) {
        this.model = model;
        this.messages = messages;
    }

}
