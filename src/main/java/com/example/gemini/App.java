package com.example.gemini;

public class App {
    public static void main(String[] args) {
        GeminiService gemini = new GeminiService();

        String response = gemini.ask("Write a haiku about Java developers.");
        System.out.println("Gemini: " + response);
    }
}
