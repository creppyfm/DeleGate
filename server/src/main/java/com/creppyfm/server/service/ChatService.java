package com.creppyfm.server.service;

import com.creppyfm.server.openai_chat_handlers.ChatMessage;
import com.creppyfm.server.openai_chat_handlers.Conversation;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatResponse;
import com.creppyfm.server.repository.ConversationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class ChatService {
    private final ConversationRepository conversationRepository;

    @Autowired
    public ChatService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public ChatMessage processMessage(String userId, ChatMessage message) throws InterruptedException, IOException, URISyntaxException {
        Conversation conversation = conversationRepository.findByUserId(userId);
        conversation.getMessages().add(message);
        conversationRepository.save(conversation);
        // Send the conversation to the OpenAI API and get the assistant's response
        return callOpenAIChat(conversation);
    }

    public ChatMessage callOpenAIChat(Conversation conversation) throws IOException, InterruptedException, URISyntaxException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Formulate the user's last message as a prompt to the AI model
        ChatMessage lastMessage = conversation.getMessages().get(conversation.getMessages().size() - 1);
        String userPrompt = lastMessage.getContent();

        // Call the method 'sendChatMessageToOpenAI()' to send the user's message and receive the AI's response
        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        //OpenAIChatResponse openAIChatResponse = openAIChatAPIManager.callOpenAIChat(con);

        // Extract the AI's response from the received OpenAIChatResponse
        //String aiReply = openAIChatResponse.getChoices().get(0).getMessage().getContent();

        // Create a new Message object with the AI's reply
        ChatMessage aiReplyMessage = new ChatMessage();
        aiReplyMessage.setRole("assistant");
        //aiReplyMessage.setContent(aiReply);

        // Return the AI's reply
        return aiReplyMessage;
    }

}
