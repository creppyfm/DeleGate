package com.creppyfm.server.controller;

import com.creppyfm.server.data_transfer_object_model.IncomingChatDataTransferObject;
import com.creppyfm.server.model.Project;
import com.creppyfm.server.openai_chat_handlers.ChatMessage;
import com.creppyfm.server.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/chat")
    public ResponseEntity<Void> processChatMessage(@SessionAttribute("userId") String userId, @RequestBody String incoming) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(incoming);
        IncomingChatDataTransferObject incomingObject = new IncomingChatDataTransferObject();
                incomingObject.setUserId(jsonNode.get("userId").asText());
                incomingObject.setTaskId(jsonNode.get("taskId").asText());
                incomingObject.setChatMessage(new ChatMessage(
                        incomingObject.getChatMessage().getRole(),
                        incomingObject.getChatMessage().getContent()
                ));
        chatService.processMessage(incomingObject, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stream")
    public SseEmitter stream() {
        return chatService.attachEmitter();
    }
}

