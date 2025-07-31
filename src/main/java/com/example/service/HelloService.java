package com.example.service;

import com.example.service.ai.AIService;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private AIService aiService;

    public HelloService(AIService aiService)
    {
        this.aiService = aiService;
    }
    public String getApplicationInfo() {
        return "AI Project Blueprint - Spring Boot Application";
    }

    public String chat(String message) {
        return aiService.chat(message, null, null);
    }
}