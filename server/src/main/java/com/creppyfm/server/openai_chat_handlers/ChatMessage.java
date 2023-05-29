package com.creppyfm.server.openai_chat_handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @JsonProperty("role")
    private String role;
    @JsonProperty("content")
    private String content;

}
