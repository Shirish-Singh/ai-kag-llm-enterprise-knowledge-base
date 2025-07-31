package com.example.service.kag;

import com.example.service.ai.AIService;
import com.example.service.nlp.EntityExtractionService;
import com.example.service.nlp.QueryEntities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KAGService {
    
    private final EntityExtractionService entityExtractionService;
    private final KnowledgeGraphQueryService knowledgeGraphQueryService;
    private final AIService aiService;
    private final CitationService citationService;
    
    public KAGResponse processQuery(String userQuery) {
        log.info("Processing KAG query: {}", userQuery);
        
        try {
            // Step 1: Extract entities and intent from user query
            QueryEntities entities = entityExtractionService.extractEntities(userQuery);
            log.info("Extracted entities: {}", entities);
            
            // Step 2: Query knowledge graph for relevant context
            KnowledgeGraphContext context = knowledgeGraphQueryService.queryKnowledgeGraph(entities);
            log.info("Retrieved knowledge graph context with {} total entities", context.getTotalEntities());
            
            // Step 3: Format context for LLM consumption
            String formattedContext = knowledgeGraphQueryService.formatContextForLLM(context);
            
            // Step 4: Generate enhanced prompt with context
            String enhancedPrompt = buildEnhancedPrompt(userQuery, formattedContext, entities);
            
            // Step 5: Get LLM response
            String llmResponse = aiService.chat(enhancedPrompt, getSystemPrompt(), null);
            
            // Step 6: Add citations and source tracking
            String responseWithCitations = citationService.addCitations(llmResponse, context);
            
            // Step 7: Build final response
            KAGResponse kagResponse = KAGResponse.builder()
                    .userQuery(userQuery)
                    .extractedEntities(entities)
                    .knowledgeGraphContext(context)
                    .llmResponse(llmResponse)
                    .responseWithCitations(responseWithCitations)
                    .citations(citationService.extractCitations(context))
                    .build();
            
            log.info("KAG response generated successfully");
            return kagResponse;
            
        } catch (Exception e) {
            log.error("Error processing KAG query: {}", e.getMessage(), e);
            return KAGResponse.builder()
                    .userQuery(userQuery)
                    .error("Failed to process query: " + e.getMessage())
                    .build();
        }
    }
    
    private String buildEnhancedPrompt(String userQuery, String context, QueryEntities entities) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are an AI assistant with access to a company's knowledge graph. ");
        prompt.append("Please answer the user's question based on the provided knowledge graph context.\n\n");
        
        prompt.append("IMPORTANT INSTRUCTIONS:\n");
        prompt.append("1. Base your answer ONLY on the provided knowledge graph context\n");
        prompt.append("2. If the context doesn't contain sufficient information, state this clearly\n");
        prompt.append("3. Include specific names, dates, and metrics from the context\n");
        prompt.append("4. Reference specific reports and documents mentioned in the context\n");
        prompt.append("5. Maintain a professional, informative tone\n\n");
        
        prompt.append("KNOWLEDGE GRAPH CONTEXT:\n");
        prompt.append(context);
        prompt.append("\n");
        
        prompt.append("USER QUERY: ").append(userQuery).append("\n\n");
        
        prompt.append("Please provide a comprehensive answer based on the above context. ");
        prompt.append("Include relevant details about people, projects, outcomes, and supporting documentation.");
        
        return prompt.toString();
    }
    
    private String getSystemPrompt() {
        return "You are a Knowledge-Augmented Generation (KAG) assistant for an enterprise. " +
               "Your role is to provide accurate, well-sourced answers about company projects, " +
               "employees, outcomes, and reports based on the knowledge graph context provided. " +
               "Always maintain factual accuracy and provide citations to source documents when possible.";
    }
}