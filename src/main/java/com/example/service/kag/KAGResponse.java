package com.example.service.kag;

import com.example.service.nlp.QueryEntities;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KAGResponse {
    
    private String userQuery;
    private QueryEntities extractedEntities;
    private KnowledgeGraphContext knowledgeGraphContext;
    private String llmResponse;
    private String responseWithCitations;
    private List<Citation> citations;
    private String error;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }
    
    public boolean hasCitations() {
        return citations != null && !citations.isEmpty();
    }
    
    public int getKnowledgeGraphEntityCount() {
        return knowledgeGraphContext != null ? knowledgeGraphContext.getTotalEntities() : 0;
    }
    
    public String getFinalResponse() {
        return responseWithCitations != null ? responseWithCitations : llmResponse;
    }
}