package com.creppyfm.server.service;

import com.creppyfm.server.data_transfer_object_model.IncomingChatDataTransferObject;
import com.creppyfm.server.model.Task;
import com.creppyfm.server.openai_chat_handlers.ChatMessage;
import com.creppyfm.server.openai_chat_handlers.Conversation;
import com.creppyfm.server.openai_chat_handlers.OpenAIChatAPIManager;
import com.creppyfm.server.repository.ConversationRepository;
import com.creppyfm.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ConversationRepository conversationRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ChatService(ConversationRepository conversationRepository, TaskRepository taskRepository) {
        this.conversationRepository = conversationRepository;
        this.taskRepository = taskRepository;
    }

    public void processMessage(IncomingChatDataTransferObject incoming) throws IOException, InterruptedException {
        String userId = incoming.getUserId();
        Task task = taskRepository.findTaskById(incoming.getTaskId());
        String prompt = "Read the following prompt, as well as the task description below, and respond accordingly. Here is the prompt: " +
                incoming.getChatMessage().getContent() + "\n";
        incoming.getChatMessage().setContent(prompt);
        ChatMessage promptMessage = incoming.getChatMessage();
        ChatMessage taskMessage = new ChatMessage("user", "Here is the task description: " + task.getDescription());
        Conversation conversation = conversationRepository.findByUserId(userId);
        if (conversation != null) {
            conversation.getMessages().add(promptMessage);
            conversation.getMessages().add(taskMessage);
            conversationRepository.save(conversation);
        } else {
            List<ChatMessage> newMessageList = new ArrayList<>();
            newMessageList.add(promptMessage);
            newMessageList.add(taskMessage);
            conversationRepository.insert(new Conversation(userId, newMessageList));
            conversation = conversationRepository.findByUserId(userId);
        }
        callOpenAIChat(conversation, userId, conversationRepository);
    }


    public void callOpenAIChat(Conversation conversation, String userId, ConversationRepository conversationRepository) throws IOException, InterruptedException {
        OpenAIChatAPIManager openAIChatAPIManager = new OpenAIChatAPIManager();
        openAIChatAPIManager.callOpenAIChat(conversation, userId, conversationRepository);
    }

}
