package com.example.service.ai;

/**
 * Abstract interface for AI/LLM service providers
 * This allows for easy integration of multiple AI providers like Ollama, OpenAI, etc.
 */
public interface AIService
{
    /**
     * Send a synchronous chat request to the AI provider
     *
     * @param message       The user message
     * @param systemMessage The system message (optional)
     * @param model         The model to use (optional, defaults to the configured model)
     * @return The AI response
     */
    String chat(String message, String systemMessage, String model);

    public String getModelName();

}