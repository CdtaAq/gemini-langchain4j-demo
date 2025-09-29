package com.example.gemini;

import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.service.AiService;
import dev.langchain4j.service.Tool;

import java.time.LocalDateTime;

public class FunctionCallingExample {

    interface Assistant {
        String chat(String message);

        @Tool("Get the current system time")
        String getCurrentTime();

        @Tool("Add two numbers together")
        int addNumbers(int a, int b);
    }

    public static void main(String[] args) {
        VertexAiGeminiChatModel model = VertexAiGeminiChatModel.builder()
                .project(System.getenv("GOOGLE_CLOUD_PROJECT"))
                .location("us-central1")
                .modelName("gemini-1.5-pro")
                .build();

        Assistant assistant = AiService.builder(Assistant.class)
                .chatLanguageModel(model)
                .build();

        // Model decides when to call functions
        System.out.println("Ask: 'What time is it now?'");
        System.out.println("Gemini: " + assistant.chat("What time is it now?"));

        System.out.println("\nAsk: 'What is 42 + 58?'");
        System.out.println("Gemini: " + assistant.chat("What is 42 + 58?"));
    }
}
