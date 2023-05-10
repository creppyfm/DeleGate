package com.creppyfm.server.openai_chat_handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenAIChatAPIManager {
    Dotenv dotenv = Dotenv.load();

    public List<String> buildsTaskList(String prompt) throws IOException, InterruptedException {
        List<String> choices = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChatMessage> messages = List.of(new ChatMessage("system", "You are the world's best project manager. " +
                        "You specialize in decomposing projects into actionable tasks. Generate a list of no more than 10 steps" +
                        " to complete the project, presented in key:value pairs."),
                new ChatMessage("user", prompt));
        OpenAIChatRequest openAIChatRequest = new OpenAIChatRequest("gpt-3.5-turbo", messages, 2000, 0); // Use the gpt-3.5-turbo model
        String input = objectMapper.writeValueAsString(openAIChatRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions")) // Use the /chat/completions endpoint
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            OpenAIChatResponse openAIChatResponse = objectMapper.readValue(response.body(), OpenAIChatResponse.class);

            if (openAIChatResponse.getChoices() != null && !openAIChatResponse.getChoices().isEmpty()) {

                OpenAIChatResponse.ChatMessageWrapper choice = openAIChatResponse.getChoices().get(0);
                String choiceString = choice.getMessage().getContent();
                choices = Arrays.stream(choiceString.split("\n")).toList();

/*
                for (OpenAIChatResponse.ChatMessageWrapper messageWrapper : openAIChatResponse.getChoices()) {
                    ChatMessage message = messageWrapper.getMessage();
                    if ("assistant".equalsIgnoreCase(message.getRole())) {
                        String task = message.getContent().replace("\n", "").trim();
                        choices.add(task);
                    }
                }
*/
            } else {
                choices.add("Sorry. I'm unable to think of any relevant tasks to complete your project. " +
                        "Try adding a bit more detail to your Project Description.");
            }
        } else {
            System.out.println("See status code below:");
            System.out.println(response.statusCode());
        }

        return choices;
    }
}
