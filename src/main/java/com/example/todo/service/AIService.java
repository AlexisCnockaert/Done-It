package com.example.todo.service;

import com.google.gson.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AIService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    public AIService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public List<String> generateSteps(String todoTitle, String userContext) {
        System.out.println("=== AI STEP GENERATION ===");
        System.out.println("Task: " + todoTitle);
        System.out.println("Context: " + userContext);
        System.out.println(apiKey);
        try {
            String prompt = buildPrompt(todoTitle, userContext);
            List<String> steps = callAI(prompt);
            if (steps.size() < 5) {
                System.out.println("Not enough steps, retrying...");
                steps = callAI(prompt + "\nReturn exactly 4-6 steps.");
            }

            if (steps.isEmpty()) {
                return generateFallbackSteps(todoTitle);
            }

            System.out.println("Generated " + steps.size() + " steps");
            return steps;

        } catch (Exception e) {
            System.err.println("AI Error: " + e.getMessage());
            return generateFallbackSteps(todoTitle);
        }
    }

    private String buildPrompt(String todoTitle, String userContext) {
        StringBuilder sb = new StringBuilder();

        sb.append("Task: ").append(todoTitle).append("\n");

        if (userContext != null && !userContext.trim().isEmpty()) {
            sb.append("Context: ").append(userContext).append("\n");
        }

        sb.append("""
                Generate 4-6 concrete, actionable steps.

                IMPORTANT RULES:
                - Return ONLY valid JSON.
                - Format: JSON array of strings.
                - No explanations, no comments, no extra text.
                - Example:
                ["Step one", "Step two", "Step three"]
                """);

        return sb.toString();
    }

    private List<String> callAI(String prompt) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content",
                "You are a task planner. Output ONLY JSON array of steps. No explanations.");
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", prompt);
        messages.add(userMsg);

        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.5);
        requestBody.addProperty("max_tokens", 300);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("HTTP-Referer", "http://localhost:3000")
                .addHeader("X-Title", "AI Todo App")
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                ))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("API error: " + response.code());
            }

            String body = response.body().string();
            System.out.println("AI Raw Response: " + body);

            JsonObject json = gson.fromJson(body, JsonObject.class);
            String content = json.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            return parseJsonSteps(content);
        }
    }

    private List<String> parseJsonSteps(String content) {
        List<String> steps = new ArrayList<>();

        try {
            JsonArray array = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement el : array) {
                steps.add(el.getAsString());
            }
        } catch (Exception e) {
            System.err.println("JSON parsing failed: " + content);
        }

        return steps;
    }

    private List<String> generateFallbackSteps(String task) {
        if (task == null) task = "this task";

        return List.of(
                "Research information about " + task,
                "Define clear goals and requirements",
                "Break the task into smaller steps",
                "Gather necessary tools or resources",
                "Execute the first actionable step",
                "Review progress and adjust the plan"
        );
    }
}
