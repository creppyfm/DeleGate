package com.creppyfm.server.openai_raw_handlers;

public record OpenAIResponse(
        String id,
        String object,
        int created,
        String model,
        ResponseChoice[] choices,
        ResponseUsage usage
) {
}
