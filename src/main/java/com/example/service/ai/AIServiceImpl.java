package com.example.service.ai;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AIServiceImpl extends AbstractAIService
{

    private final ChatModel chatModel;


    //use @Qualifier to specify the bean name for the chat model
    //example: @Qualifier("openAIChatModel")
    @Autowired
    public AIServiceImpl(
        ChatModel chatModel)
    {
        this.chatModel = chatModel;
    }


    @Override
    public String chat(String message, String systemMessage, String model)
    {
        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String requestId = String.valueOf(System.nanoTime());
            logLLMRequest(requestId, timestamp, message, systemMessage, model);
            Prompt prompt = new Prompt(message);
            return chatModel.call(prompt).getResult().getOutput().getText();
        } catch (Exception e) {
            log.error("Error in synchronous chat call: {}", e.getMessage(), e);
            return "Error calling AI service: " + e.getMessage();
        }
    }

    @Override
    public String getModelName()
    {
        return chatModel.getDefaultOptions().getModel().toUpperCase();
    }

}