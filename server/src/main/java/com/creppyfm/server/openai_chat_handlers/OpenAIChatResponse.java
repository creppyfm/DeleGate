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
public class OpenAIChatResponse {
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
    private List<ChatMessageWrapper> choices;
    @JsonProperty("choices")
    private List<ChatWrapper> chatChoices;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatMessageWrapper {
        @JsonProperty("message")
        private ChatMessage message;

        public ChatMessage getMessage() {
            return message;
        }

        public void setMessage(ChatMessage message) {
            this.message = message;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatWrapper {
        @JsonProperty("delta")
        private ChatMessage delta;

        public ChatMessage getDelta() {
            return delta;
        }
        public void setDelta(ChatMessage message) {
            this.delta = message;
        }
    }


    /*
    * To see raw output for testing purposes.
    * */
    @Override
    public String toString() {
        return this.choices.get(0).getMessage().getContent();
    }
}

@Data
class Usage {
    @JsonProperty("prompt_tokens")
    private int promptTokens;

    @JsonProperty("completion_tokens")
    private int completionTokens;

    @JsonProperty("total_tokens")
    private int totalTokens;


}
