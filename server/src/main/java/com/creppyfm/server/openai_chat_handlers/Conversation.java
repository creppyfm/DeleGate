package com.creppyfm.server.openai_chat_handlers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Conversation {
    private String userId;
    private List<ChatMessage> messages;
}
