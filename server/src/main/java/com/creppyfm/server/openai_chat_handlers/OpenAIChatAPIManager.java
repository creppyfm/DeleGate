package com.creppyfm.server.openai_chat_handlers;

import com.creppyfm.server.data_transfer_object_model.ProjectDataTransferObject;
import com.creppyfm.server.model.Task;
//import com.fasterxml.jackson.datatype.jsr310.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

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

    public ProjectDataTransferObject buildsProjectDataTransferObject(String prompt) {
        ProjectDataTransferObject projectDTO = new ProjectDataTransferObject();
        return projectDTO;
    }

    public Map<String, String> delegateTasks(Map<String, List<String>> memberStrengths, List<Task> tasks) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", "You are the world's best project manager. You specialize in delegating " +
                        "tasks to members of a team based on the strengths of each team member and the skills needed to complete a particular task. " +
                        "Analyze the strengths of the team members provided in memberStrengths, cross reference them with the tasks provided, and " +
                        "assign the tasks to team members accordingly. Keep in mind, a team member can be assigned more than one task, but no one " +
                        "team member should be assigned more than 4 in total." +
                        "NOTE: Do not include any conversational phrases or sentences in your response. " +
                        "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                        "or ending your response. ONLY return the requested mapping of team members to their assigned tasks. " +
                        "NOTE: The response should take the form of \"Key\": \"Value\" pairs, as demonstrated below.\n" +
                        "Here is an example of the format your response must adhere to: " +
                        "  \"645aaf0c96c60d24d659e7f9\": \"645c09a7d8297258cc42bca4\",\n" +
                        "  \"645aaf0c96c60d24d659e7fa\": \"645c09fcd8297258cc42bca6\",\n" +
                        "  \"645aaf0c96c60d24d659e7fb\": \"645c09e2d8297258cc42bca5\",\n" +
                        "  \"645aaf0c96c60d24d659e7fc\": \"645c09e2d8297258cc42bca5\" \n" +
                        "where the key represents the task id number, and the value represents the corresponding project member id."

                ),
                new ChatMessage("user", objectMapper.writeValueAsString(Map.of("memberStrengths", memberStrengths, "tasks", tasks)))
        );

        OpenAIChatRequest openAIChatRequest = new OpenAIChatRequest("gpt-3.5-turbo", messages, 2000, 0);
        String input = objectMapper.writeValueAsString(openAIChatRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
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
                System.out.println(choiceString);
                return objectMapper.readValue(choiceString, new TypeReference<Map<String, String>>() {});
            }
        } else {
            System.out.println("See status code below:");
            System.out.println(response.statusCode());
        }

        return new HashMap<>();
    }
}
