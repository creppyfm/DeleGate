package com.creppyfm.server.openai_chat_handlers;

import java.util.List;

public class OpenAIChatRequest {
    private String model;
    private List<ChatMessage> messages;

    public OpenAIChatRequest(String model, List<ChatMessage> messages, int max_tokens, int temperature) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
