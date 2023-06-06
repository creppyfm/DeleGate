package com.creppyfm.server.controller;

import com.creppyfm.server.openai_chat_handlers.ChatMessage;
import com.creppyfm.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@CrossOrigin
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{userId}")
    @SendTo("/topic/messages/{userId}")
    public ChatMessage send(@DestinationVariable String userId, ChatMessage message) throws IOException, URISyntaxException, InterruptedException {
        return chatService.processMessage(userId, message);
    }
}
