package com.creppyfm.server.openai_chat_handlers;

import com.creppyfm.server.data_transfer_object_model.ProjectDataTransferObject;
import com.creppyfm.server.data_transfer_object_model.StepDataTransferObject;
import com.creppyfm.server.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class OpenAIChatAPIManager {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    Dotenv dotenv = Dotenv.load();


    public List<List<String>> buildsTaskList(String prompt) throws IOException, InterruptedException {
        List<List<String>> tasks = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChatMessage> messages = List.of(new ChatMessage("system", "You are the world's best project manager. " +
                        "You specialize in decomposing project steps or tasks into detailed, actionable tasks or subtasks. "), //removed: "Generate a list of no more than 10 tasks to complete the step."
                new ChatMessage("user", prompt));
        OpenAIChatRequest openAIChatRequest = new OpenAIChatRequest("gpt-3.5-turbo", messages, 4000, 0); //use the gpt-3.5-turbo model
        String input = objectMapper.writeValueAsString(openAIChatRequest);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL)) // Use the /chat/completions endpoint
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        int maxTries = 3;
        int currTries = 0;
        boolean successfulResponse = false;

        while (!successfulResponse && currTries < maxTries){
            try {
                var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    OpenAIChatResponse openAIChatResponse = objectMapper.readValue(response.body(), OpenAIChatResponse.class);

                    //TESTING
                    System.out.println(openAIChatResponse.toString());

                    if (openAIChatResponse.getChoices() != null && !openAIChatResponse.getChoices().isEmpty()) {
                        OpenAIChatResponse.ChatMessageWrapper choice = openAIChatResponse.getChoices().get(0);
                        String choiceString = choice.getMessage().getContent();

                        //parse the JSON string into a List<List<Strings>>
                        tasks = objectMapper.readValue(choiceString, new TypeReference<List<List<String>>>() {});
                        successfulResponse = true;

                        //counting failed attempts
                        System.out.println("\n\n\nNumber of tries: " + currTries + "\n\n\n");


                    } else {
                        tasks.add(List.of("Sorry. I'm unable to think of any relevant tasks to complete your project. Try adding a bit more detail to your Project Description."));
                    }
                } else {
                    System.out.println("See status code below:");
                    System.out.println(response.statusCode());
                }
            } catch (JsonProcessingException e) {
                currTries++;
                if (currTries == maxTries) {
                    System.out.println("Unable to parse response JSON: " + e.getMessage());
                }
            }
        }
        return tasks;
    }

    public ProjectDataTransferObject buildsProjectDataTransferObject(String prompt) throws IOException, URISyntaxException, InterruptedException {
        OpenAIChatResponse openAIChatResponse = sendChatMessageToOpenAI(prompt);

        //TESTING
        System.out.println(openAIChatResponse.toString());

        //get the responseMessage as a JSON string
        String responseMessage = openAIChatResponse.getChoices().get(0).getMessage().getContent();

        //parse the JSON string into a JSON object
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseMessage);

        ProjectDataTransferObject projectDTO = new ProjectDataTransferObject();

        projectDTO.setTitle(responseJson.get("title").asText());
        projectDTO.setDescription(responseJson.get("description").asText());

        List<StepDataTransferObject> steps = new ArrayList<>();
        for (JsonNode stepArray : responseJson.get("steps")) {
            String stepTitle = stepArray.get(0).asText();
            String stepDescription = stepArray.get(1).asText();
            steps.add(new StepDataTransferObject(stepTitle, stepDescription));
        }

        projectDTO.setSteps(steps);

        return projectDTO;
    }

    private OpenAIChatResponse sendChatMessageToOpenAI(String prompt) throws IOException, InterruptedException, URISyntaxException {
        String promptToSend = "Your task is to analyze the prompt provided below, create a project title, create a project description, " +
                "and create a list of no more than 10 high-level steps to complete the project. " +
                "The format by which you return your response must match the following JSON structure:\n " +
                "{\"title\": \"Your project title\",\n" +
                " \"description\": \"Your project description\", \n" +
                "\"steps\": [\n" +
                "[\"Step one title\", \"Step one description\"],\n" +
                "[\"Step two title\", \"Step two description\"],\n" +
                "...]}\n" +
                "In the steps array, each subarray should contain two elements. The first element of the subarray should be the step's title, " +
                "and the second should be the step's description. " +
                "Below is an example of the required format for your response: \n" +
                "{\"title\": \"Example project title\",\n" +
                "\"description\": \"Example project description\", \n" +
                "\"steps\": [\n" +
                "[\"Step one title\", \"Step one description\"],\n" +
                "[\"Step two title\", \"Step two description\"],\n" +
                "[\"Step three title\", \"Step three description\"]\n" +
                "...]\n" +
                "}" +
                "\"NOTE: Do not include any conversational phrases or sentences in your response. " +
                "Do not include phrases such as \"Sure, I can do that,\" or any phrases throughout " +
                "or ending your response. Do not include numbering for the steps. " +
                "ONLY return the project title, description, and steps in the required format. " +
                "Here is your prompt:\n" + prompt;

        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequest request;
        HttpResponse<String> response;
        OpenAIChatResponse openAIChatResponse = new OpenAIChatResponse();

        List<ChatMessage> messages = List.of(
                new ChatMessage("system", "You are the world's best project manager AI. You specialize in " +
                    "analyzing brief prompts containing needs and requirements, and producing project titles, " +
                    "project descriptions, and high-level steps necessary to complete the project. "),
                new ChatMessage("user", promptToSend));

        OpenAIChatRequest chatRequest = new OpenAIChatRequest("gpt-3.5-turbo", messages, 3000, 0);

        String requestBody = objectMapper.writeValueAsString(chatRequest);

        request = HttpRequest.newBuilder()
                .uri(new URI(OPENAI_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        int maxTries = 3;
        int currTries = 0;
        boolean successfulResponse = false;

        while (!successfulResponse && currTries < maxTries) {
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                openAIChatResponse = objectMapper.readValue(response.body(), OpenAIChatResponse.class);
                successfulResponse = true;

                //counting failed attempts
                System.out.println("\n\n\nNumber of tries: " + currTries + "\n\n\n");

            } catch (JsonProcessingException e) {
                currTries++;
                if (currTries == maxTries) {
                    System.out.println("Unable to parse response JSON: " + e.getMessage());
                }
            }
        }

        if(openAIChatResponse == null) {
            throw new IOException("Unable to parse OpenAI response after multiple attempts.");
        }

        return openAIChatResponse;
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
                .uri(URI.create(OPENAI_URL))
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
