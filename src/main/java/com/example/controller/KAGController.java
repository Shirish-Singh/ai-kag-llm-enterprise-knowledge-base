package com.example.controller;

import com.example.service.kag.KAGService;
import com.example.service.kag.KAGResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kag")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Knowledge-Augmented Generation", description = "KAG API for querying enterprise knowledge graph")
public class KAGController {
    
    private final KAGService kagService;
    
    @PostMapping("/query")
    @Operation(
        summary = "Process KAG Query",
        description = "Processes a natural language query using Knowledge-Augmented Generation. " +
                     "The system extracts entities, queries the knowledge graph, and generates " +
                     "an AI response with citations and source tracking."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Query processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid query format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<KAGResponse> processQuery(
            @Parameter(description = "Natural language query about company knowledge", 
                      example = "Who in our company has worked on AI safety and what were the main outcomes?")
            @RequestBody QueryRequest request) {
        
        log.info("Received KAG query: {}", request.getQuery());
        
        try {
            if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(KAGResponse.builder()
                        .userQuery(request.getQuery())
                        .error("Query cannot be empty")
                        .build());
            }
            
            KAGResponse response = kagService.processQuery(request.getQuery());
            
            if (response.hasError()) {
                log.error("KAG processing error: {}", response.getError());
                return ResponseEntity.internalServerError().body(response);
            }
            
            log.info("KAG query processed successfully with {} knowledge graph entities", 
                    response.getKnowledgeGraphEntityCount());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Unexpected error processing KAG query: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(KAGResponse.builder()
                    .userQuery(request.getQuery())
                    .error("Unexpected error: " + e.getMessage())
                    .build());
        }
    }
    
    @GetMapping("/query")
    @Operation(
        summary = "Process KAG Query (GET)",
        description = "Simple GET endpoint for testing KAG queries with query parameter"
    )
    public ResponseEntity<KAGResponse> processQueryGet(
            @Parameter(description = "Natural language query", 
                      example = "Who worked on AI safety?")
            @RequestParam String q) {
        
        QueryRequest request = new QueryRequest();
        request.setQuery(q);
        return processQuery(request);
    }
    
    @GetMapping("/examples")
    @Operation(
        summary = "Get Example Queries",
        description = "Returns a list of example queries that work well with the current knowledge graph"
    )
    public ResponseEntity<ExampleQueries> getExampleQueries() {
        ExampleQueries examples = new ExampleQueries();
        examples.addExample(
            "Who in our company has worked on AI safety and what were the main outcomes?",
            "Comprehensive query about AI safety projects, team members, and achievements"
        );
        examples.addExample(
            "What are the key outcomes from our AI safety initiatives?",
            "Focus on outcomes and achievements from AI safety projects"
        );
        examples.addExample(
            "Who worked on the AI Safety Blueprint project?",
            "Find team members involved in specific project"
        );
        examples.addExample(
            "What reports document our AI bias reduction efforts?",
            "Find documentation and reports related to bias detection"
        );
        examples.addExample(
            "Show me the collaboration between Alice Smith and Bob Lee",
            "Find collaborative work between specific employees"
        );
        
        return ResponseEntity.ok(examples);
    }
    
    // Inner classes for request/response DTOs
    public static class QueryRequest {
        private String query;
        
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
    }
    
    public static class ExampleQueries {
        private java.util.List<ExampleQuery> examples = new java.util.ArrayList<>();
        
        public void addExample(String query, String description) {
            examples.add(new ExampleQuery(query, description));
        }
        
        public java.util.List<ExampleQuery> getExamples() { return examples; }
        public void setExamples(java.util.List<ExampleQuery> examples) { this.examples = examples; }
        
        public static class ExampleQuery {
            private String query;
            private String description;
            
            public ExampleQuery() {}
            public ExampleQuery(String query, String description) {
                this.query = query;
                this.description = description;
            }
            
            public String getQuery() { return query; }
            public void setQuery(String query) { this.query = query; }
            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }
    }
}