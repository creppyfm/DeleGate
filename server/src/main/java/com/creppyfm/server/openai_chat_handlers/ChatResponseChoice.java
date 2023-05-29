package com.creppyfm.server.openai_chat_handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseChoice {
    private String role;
    private String content;

}
