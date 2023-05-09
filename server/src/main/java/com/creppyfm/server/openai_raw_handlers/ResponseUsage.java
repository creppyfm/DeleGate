package com.creppyfm.server.openai_raw_handlers;

public record ResponseUsage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
) {
}
