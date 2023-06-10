package com.creppyfm.server.data_transfer_object_model;

import com.creppyfm.server.openai_chat_handlers.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomingChatDataTransferObject {
    private String userId;
    private String taskId;
    private ChatMessage chatMessage;
}
