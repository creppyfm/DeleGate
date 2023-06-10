package com.creppyfm.server.openai_chat_handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Conversation")
public class Conversation {
    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    private List<ChatMessage> messages;

    public Conversation(String userId, List<ChatMessage> messages) {
        this.userId = userId;
        this.messages = messages;
    }

    public List<ChatMessage> getMessages() {
        return Objects.requireNonNullElseGet(messages, ArrayList::new);
    }
}
