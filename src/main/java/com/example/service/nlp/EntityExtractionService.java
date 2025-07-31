package com.example.service.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntityExtractionService {
    
    private StanfordCoreNLP pipeline;
    private final Set<String> projectKeywords;
    private final Set<String> employeeKeywords;
    private final Set<String> outcomeKeywords;
    private final Set<String> reportKeywords;
    
    public EntityExtractionService() {
        // Initialize domain-specific keywords for our use case
        this.projectKeywords = Set.of(
            "project", "projects", "initiative", "initiatives", "program", "programs",
            "ai safety", "bias detection", "ethics framework", "safety blueprint",
            "framework", "system", "platform", "implementation"
        );
        
        this.employeeKeywords = Set.of(
            "employee", "employees", "person", "people", "team", "member", "members",
            "researcher", "engineer", "specialist", "manager", "lead", "developer",
            "worked", "working", "involved", "participated", "contributed"
        );
        
        this.outcomeKeywords = Set.of(
            "outcome", "outcomes", "result", "results", "achievement", "achievements",
            "impact", "success", "benefit", "improvement", "reduction", "increase",
            "metrics", "performance", "effectiveness", "accomplished", "delivered"
        );
        
        this.reportKeywords = Set.of(
            "report", "reports", "document", "documents", "documentation", "paper",
            "assessment", "analysis", "study", "findings", "publication", "summary"
        );
    }
    
    @PostConstruct
    public void initializePipeline() {
        try {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
            props.setProperty("ner.useSUTime", "false");
            props.setProperty("ner.applyNumericClassifiers", "false");
            this.pipeline = new StanfordCoreNLP(props);
            log.info("NLP pipeline initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize NLP pipeline: {}", e.getMessage());
            // Continue without NLP pipeline - use keyword-based extraction as fallback
        }
    }
    
    public QueryEntities extractEntities(String query) {
        log.info("Extracting entities from query: {}", query);
        
        QueryEntities entities = new QueryEntities();
        String lowerQuery = query.toLowerCase();
        
        // Extract entity types and keywords using both NLP and keyword matching
        entities.setEmployeeKeywords(extractEmployeeKeywords(lowerQuery));
        entities.setProjectKeywords(extractProjectKeywords(lowerQuery));
        entities.setOutcomeKeywords(extractOutcomeKeywords(lowerQuery));
        entities.setReportKeywords(extractReportKeywords(lowerQuery));
        
        // Use NLP pipeline if available for person names and organizations
        if (pipeline != null) {
            try {
                entities.setPersonNames(extractPersonNames(query));
                entities.setOrganizations(extractOrganizations(query));
            } catch (Exception e) {
                log.warn("NLP extraction failed, using keyword-based approach: {}", e.getMessage());
            }
        }
        
        // Determine query intent
        entities.setQueryIntent(determineQueryIntent(lowerQuery));
        
        log.info("Extracted entities: {}", entities);
        return entities;
    }
    
    private Set<String> extractEmployeeKeywords(String query) {
        Set<String> keywords = new HashSet<>();
        
        for (String keyword : employeeKeywords) {
            if (query.contains(keyword)) {
                keywords.add(keyword);
            }
        }
        
        // Look for specific skills or roles
        if (query.contains("ai safety") || query.contains("safety")) {
            keywords.add("AI Safety");
        }
        if (query.contains("researcher")) {
            keywords.add("researcher");
        }
        if (query.contains("engineer")) {
            keywords.add("engineer");
        }
        
        return keywords;
    }
    
    private Set<String> extractProjectKeywords(String query) {
        Set<String> keywords = new HashSet<>();
        
        for (String keyword : projectKeywords) {
            if (query.contains(keyword)) {
                keywords.add(keyword);
            }
        }
        
        // Look for specific project categories
        if (query.contains("ai safety") || query.contains("safety")) {
            keywords.add("AI Safety");
        }
        if (query.contains("bias")) {
            keywords.add("bias");
        }
        if (query.contains("ethics")) {
            keywords.add("ethics");
        }
        
        return keywords;
    }
    
    private Set<String> extractOutcomeKeywords(String query) {
        Set<String> keywords = new HashSet<>();
        
        for (String keyword : outcomeKeywords) {
            if (query.contains(keyword)) {
                keywords.add(keyword);
            }
        }
        
        // Look for specific outcome types
        if (query.contains("reduction") || query.contains("reduced")) {
            keywords.add("reduction");
        }
        if (query.contains("improvement") || query.contains("improved")) {
            keywords.add("improvement");
        }
        if (query.contains("accuracy")) {
            keywords.add("accuracy");
        }
        
        return keywords;
    }
    
    private Set<String> extractReportKeywords(String query) {
        Set<String> keywords = new HashSet<>();
        
        for (String keyword : reportKeywords) {
            if (query.contains(keyword)) {
                keywords.add(keyword);
            }
        }
        
        return keywords;
    }
    
    private Set<String> extractPersonNames(String text) {
        Set<String> personNames = new HashSet<>();
        
        if (pipeline == null) return personNames;
        
        try {
            CoreDocument document = new CoreDocument(text);
            pipeline.annotate(document);
            
            for (CoreLabel token : document.tokens()) {
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if ("PERSON".equals(ner)) {
                    personNames.add(token.word());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract person names: {}", e.getMessage());
        }
        
        return personNames;
    }
    
    private Set<String> extractOrganizations(String text) {
        Set<String> organizations = new HashSet<>();
        
        if (pipeline == null) return organizations;
        
        try {
            CoreDocument document = new CoreDocument(text);
            pipeline.annotate(document);
            
            for (CoreLabel token : document.tokens()) {
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if ("ORGANIZATION".equals(ner)) {
                    organizations.add(token.word());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract organizations: {}", e.getMessage());
        }
        
        return organizations;
    }
    
    private QueryIntent determineQueryIntent(String query) {
        // Analyze query structure to determine intent
        if (query.contains("who") && (query.contains("worked") || query.contains("involved"))) {
            return QueryIntent.FIND_PEOPLE_BY_PROJECT;
        }
        
        if (query.contains("what") && (query.contains("outcome") || query.contains("result") || query.contains("achievement"))) {
            return QueryIntent.FIND_OUTCOMES;
        }
        
        if (query.contains("project") && (query.contains("outcome") || query.contains("result"))) {
            return QueryIntent.FIND_PROJECT_OUTCOMES;
        }
        
        if (query.contains("report") || query.contains("document")) {
            return QueryIntent.FIND_REPORTS;
        }
        
        // Default to comprehensive search if unclear
        return QueryIntent.COMPREHENSIVE_SEARCH;
    }
}