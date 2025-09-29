package com.example.gemini;

import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiService;

public class GeminiService {

    interface Assistant {
        String chat(String message);
    }

    private final Assistant assistant;

    public GeminiService() {
        // Initialize Gemini model
        VertexAiGeminiChatModel model = VertexAiGeminiChatModel.builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT")) // GCP project ID
                .location("us-central1") // or your preferred region
                .modelName("gemini-1.5-pro") // or gemini-1.5-flash
                .build();

        // Bind model to interface
        assistant = AiService.builder(Assistant.class)
                .chatLanguageModel(model)
                .build();
    }

    public String ask(String question) {
        return assistant.chat(question);
    }
}
