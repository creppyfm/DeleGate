package com.creppyfm.server.openai_chat_handlers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIStreamResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("object")
    private String object;
    @JsonProperty("created")
    private int created;
    @JsonProperty("model")
    private String model;
    @JsonProperty("usage")
    private Usage usage;
    @JsonProperty("choices")
    private List<ChatWrapper> chatChoices;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatWrapper {
        @JsonProperty("delta")
        private ChatMessage delta;
        @JsonProperty("finish_reason")
        private String finishReason;
        /*
        * Possibly add 'finish_reason' field to check if end of stream?
        * Value would be "stop"
        * This would replace if (!"[DONE]".equals(response.getId()))
        * */
    }

    /*
     * To see raw output for testing purposes.
     * */
    @Override
    public String toString() {
        return this.chatChoices.get(0).getDelta().getContent();
    }

}

