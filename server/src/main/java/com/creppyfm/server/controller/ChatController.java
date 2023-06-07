package com.creppyfm.server.controller;

import com.creppyfm.server.data_transfer_object_model.IncomingChatDataTransferObject;
import com.creppyfm.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@CrossOrigin
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat/")
    public SseEmitter send(@RequestBody IncomingChatDataTransferObject incoming) throws IOException, InterruptedException {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitter = chatService.processMessage(incoming, sseEmitter);
        return sseEmitter;
    }
}
