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
        log.info("=== KAG SERVICE PROCESSING START ===");
        log.info("User query: '{}'", userQuery);
        
        try {
            // Step 1: Extract entities and intent from user query
            log.info("STEP 1: Starting entity extraction");
            QueryEntities entities = entityExtractionService.extractEntities(userQuery);
            log.info("STEP 1 COMPLETE: Entity extraction finished");
            
            // Step 2: Query knowledge graph for relevant context
            log.info("STEP 2: Starting knowledge graph query");
            KnowledgeGraphContext context = knowledgeGraphQueryService.queryKnowledgeGraph(entities);
            log.info("STEP 2 COMPLETE: Knowledge graph query finished with {} total entities", context.getTotalEntities());
            
            // Step 3: Format context for LLM consumption
            log.info("STEP 3: Formatting context for LLM");
            String formattedContext = knowledgeGraphQueryService.formatContextForLLM(context);
            log.info("STEP 3 COMPLETE: Context formatted. Length: {} characters", formattedContext.length());
            log.debug("Formatted context: {}", formattedContext);
            
            // Step 4: Generate enhanced prompt with context
            log.info("STEP 4: Building enhanced prompt");
            String enhancedPrompt = buildEnhancedPrompt(userQuery, formattedContext, entities);
            log.info("STEP 4 COMPLETE: Enhanced prompt built. Length: {} characters", enhancedPrompt.length());
            log.debug("Enhanced prompt: {}", enhancedPrompt);
            
            // Step 5: Get LLM response
            log.info("STEP 5: Calling AI service");
            String llmResponse = aiService.chat(enhancedPrompt, getSystemPrompt(), null);
            log.info("STEP 5 COMPLETE: AI service response received. Length: {} characters", llmResponse.length());
            log.debug("LLM response: {}", llmResponse);
            
            // Step 6: Add citations and source tracking
            log.info("STEP 6: Adding citations");
            String responseWithCitations = citationService.addCitations(llmResponse, context);
            log.info("STEP 6 COMPLETE: Citations added");
            
            // Step 7: Build final response
            log.info("STEP 7: Building final KAG response");
            KAGResponse kagResponse = KAGResponse.builder()
                    .userQuery(userQuery)
                    .extractedEntities(entities)
                    .knowledgeGraphContext(context)
                    .llmResponse(llmResponse)
                    .responseWithCitations(responseWithCitations)
                    .citations(citationService.extractCitations(context))
                    .build();
            
            log.info("=== KAG SERVICE PROCESSING COMPLETE ===");
            log.info("Final response has {} citations and context with {} total entities", 
                    kagResponse.getCitations() != null ? kagResponse.getCitations().size() : 0,
                    context.getTotalEntities());
            
            return kagResponse;
            
        } catch (Exception e) {
            log.error("=== KAG SERVICE PROCESSING FAILED ===");
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