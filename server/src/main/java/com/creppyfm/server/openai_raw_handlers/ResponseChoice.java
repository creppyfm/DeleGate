package com.creppyfm.server.openai_raw_handlers;

public record ResponseChoice(
        String text,
        int index,
        Object logprobs,
        String finish_reason
) {
}
