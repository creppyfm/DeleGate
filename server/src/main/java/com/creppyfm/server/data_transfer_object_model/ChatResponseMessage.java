package com.creppyfm.server.data_transfer_object_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseMessage {
    private String role;
    private String content;
    private String finishReason;
}
