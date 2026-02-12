package com.example.todo.service;

import com.google.gson.*;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AIService {
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${ia.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url:https://openrouter.ai/api/v1/chat/completions}")
    private String apiUrl;

    @Value("${openrouter.model:arcee-ai/trinity-large-preview:free}")
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
        logger.info("=== AI STEP GENERATION ===");
        logger.info("Task: {}", todoTitle);
        logger.info("Context: {}", userContext);
        logger.info("API KEY = {}", apiKey);
        logger.info("API URL = {}", apiUrl);
        logger.info("MODEL = {}", model);
        try {
            String prompt = buildPrompt(todoTitle, userContext);
            List<String> steps = callAI(prompt);
            if (steps.size() < 4) {
                logger.info("Not enough steps, retrying...");
                steps = callAI(prompt + "\nReturn exactly 4-6 steps.");
            }

            if (steps.isEmpty()) {
                return generateFallbackSteps(todoTitle);
            }

            logger.info("Generated {} steps", steps.size());
            return steps;

        } catch (Exception e) {
            logger.error("AI Error: ", e);
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
                logger.error("API error: HTTP {}", response.code());
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                logger.error("Error response: {}", errorBody);
                throw new IOException("API error: " + response.code());
            }

            String body = response.body().string();
            logger.debug("AI Raw Response: {}", body);

            JsonObject json = gson.fromJson(body, JsonObject.class);
            
            // Validate response structure
            if (json == null) {
                logger.error("Failed to parse JSON response");
                throw new IOException("Invalid JSON response from API");
            }
            
            JsonArray choices = json.getAsJsonArray("choices");
            if (choices == null || choices.size() == 0) {
                logger.error("No choices in API response. Full response: {}", json);
                throw new IOException("Invalid API response: missing choices array");
            }
            
            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            if (firstChoice == null) {
                logger.error("First choice is null");
                throw new IOException("Invalid API response: malformed choice object");
            }
            
            JsonObject message = firstChoice.getAsJsonObject("message");
            if (message == null) {
                logger.error("Message object is null in first choice");
                throw new IOException("Invalid API response: missing message object");
            }
            
            JsonElement contentElement = message.get("content");
            if (contentElement == null || contentElement.isJsonNull()) {
                logger.error("Content is null in message");
                throw new IOException("Invalid API response: missing content");
            }
            
            String content = contentElement.getAsString();
            logger.info("AI Response content: {}", content);
            
            return parseJsonSteps(content);
        }
    }

    private List<String> parseJsonSteps(String content) {
        List<String> steps = new ArrayList<>();

        if (content == null || content.trim().isEmpty()) {
            logger.error("Content is empty or null");
            return steps;
        }

        try {
            JsonElement element = JsonParser.parseString(content);
            
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                for (JsonElement el : array) {
                    if (el.isJsonPrimitive()) {
                        steps.add(el.getAsString());
                    } else {
                        logger.warn("Skipping non-primitive element: {}", el);
                    }
                }
            } else if (element.isJsonObject()) {
                logger.warn("Content is JSON object, not array: {}", content);
            } else {
                logger.error("Content is not JSON array or object: {}", content);
            }
            
            logger.info("Successfully parsed {} steps from AI response", steps.size());
            
        } catch (JsonSyntaxException e) {
            logger.error("JSON parsing failed for content: {}", content, e);
        } catch (Exception e) {
            logger.error("Unexpected error parsing steps: {}", content, e);
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
