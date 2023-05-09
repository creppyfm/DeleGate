package com.creppyfm.server.openai_raw_handlers;

public record OpenAIRequest(
        String model,
        String prompt,
        int temperature,
        int max_tokens) {
}
