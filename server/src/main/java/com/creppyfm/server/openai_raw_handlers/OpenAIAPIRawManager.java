package com.creppyfm.server.openai_raw_handlers;

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

public class OpenAIAPIRawManager {

    Dotenv dotenv = Dotenv.load();

    public List<String> buildsTaskList(String prompt) throws IOException, InterruptedException {
        List<String> choices = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        OpenAIRequest openAIRequest = new OpenAIRequest("text-completions-003", prompt, 0, 2000);
        String input = objectMapper.writeValueAsString(openAIRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            OpenAIResponse openAIResponse = objectMapper.readValue(response.body(), OpenAIResponse.class);

            if (openAIResponse.choices().length > 0) {
                ResponseChoice choice = openAIResponse.choices()[openAIResponse.choices().length - 1];
                String choiceString = choice.text();
                choices = Arrays.stream(choiceString.split("\n")).toList();
            } else {
                choices.add("Sorry. I'm unable to think of any relevant tasks to complete your project. " +
                        "Try adding a bit more detail to your Project Description.");
            }

        } else {
            System.out.println("See status code below");
            System.out.println(response.statusCode());
        }

        return choices;
    }
}
