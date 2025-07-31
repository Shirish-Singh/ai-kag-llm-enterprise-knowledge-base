package com.example.service.ai;

import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAIService implements AIService
{
    protected static final String LOG_SEPARATOR = "=".repeat(80);
    protected static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Log LLM request details
     */
    protected void logLLMRequest(String requestId, String timestamp, String message, String systemMessage, String model) {
        log.debug("\n" + LOG_SEPARATOR);
        log.debug("🤖 LLM REQUEST [{}] - {}", requestId, timestamp);
        log.debug(LOG_SEPARATOR);
        log.debug("📋 MODEL: {}", (model != null ? model : getModelName()));
        log.debug("🏢 PROVIDER: Ollama");

        if (systemMessage != null && !systemMessage.isEmpty()) {
            log.debug("🎯 SYSTEM MESSAGE:");
            log.debug("{}", systemMessage);
            log.debug("{}", "-".repeat(40));
        }

        log.debug("💬 USER MESSAGE:");
        log.debug("{}", message);
        log.debug(LOG_SEPARATOR);

        log.info("LLM Request [{}] - Model: {}, Message length: {}, System message: {}",
            requestId, (model != null ? model : getModelName()), message.length(),
            systemMessage != null && !systemMessage.isEmpty());
    }

    /**
     * Log LLM response details
     */
    protected void logLLMResponse(String requestId, String timestamp, String response, long duration) {
        log.debug("\n" + LOG_SEPARATOR);
        log.debug("🤖 LLM RESPONSE [{}] - {}", requestId, timestamp);
        log.debug(LOG_SEPARATOR);
        log.debug("⏱️ DURATION: {}ms", duration);
        log.debug("📝 RESPONSE LENGTH: {} characters", response.length());
        log.debug("{}", "-".repeat(40));
        log.debug("💭 RESPONSE:");
        log.debug("{}", response);
        log.debug("{}\n", LOG_SEPARATOR);

        log.info("LLM Response [{}] - Duration: {}ms, Response length: {}",
            requestId, duration, response.length());
    }

    /**
     * Log LLM error details
     */
    protected void logLLMError(String requestId, String timestamp, String errorMsg, Exception e) {
        log.debug("\n" + LOG_SEPARATOR);
        log.debug("❌ LLM ERROR [{}] - {}", requestId, timestamp);
        log.debug(LOG_SEPARATOR);
        log.debug("🚨 ERROR MESSAGE: {}", errorMsg);
        log.debug("🔍 EXCEPTION: {}", e.getClass().getSimpleName());
        log.debug("{}\n", LOG_SEPARATOR);

        log.error("LLM Error [{}] - {}", requestId, errorMsg, e);
    }
}
