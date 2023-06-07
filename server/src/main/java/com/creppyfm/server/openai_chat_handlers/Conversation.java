package com.creppyfm.server.openai_chat_handlers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Conversation {
    private String userId;
    private List<ChatMessage> messages;

    public List<ChatMessage> getMessages() {
        return Objects.requireNonNullElseGet(messages, ArrayList::new);
    }
}
