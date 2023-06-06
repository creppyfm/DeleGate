package com.creppyfm.server.repository;

import com.creppyfm.server.openai_chat_handlers.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Conversation findByUserId(String userId);
}
