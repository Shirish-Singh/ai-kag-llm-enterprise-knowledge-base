package com.example.service.nlp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryEntities {
    
    private Set<String> employeeKeywords = new HashSet<>();
    private Set<String> projectKeywords = new HashSet<>();
    private Set<String> outcomeKeywords = new HashSet<>();
    private Set<String> reportKeywords = new HashSet<>();
    
    private Set<String> personNames = new HashSet<>();
    private Set<String> organizations = new HashSet<>();
    
    private QueryIntent queryIntent = QueryIntent.COMPREHENSIVE_SEARCH;
    
    public boolean hasEmployeeKeywords() {
        return !employeeKeywords.isEmpty() || !personNames.isEmpty();
    }
    
    public boolean hasProjectKeywords() {
        return !projectKeywords.isEmpty();
    }
    
    public boolean hasOutcomeKeywords() {
        return !outcomeKeywords.isEmpty();
    }
    
    public boolean hasReportKeywords() {
        return !reportKeywords.isEmpty();
    }
    
    public String getPrimaryProjectCategory() {
        if (projectKeywords.contains("AI Safety") || employeeKeywords.contains("AI Safety")) {
            return "AI Safety";
        }
        if (projectKeywords.contains("bias")) {
            return "bias";
        }
        if (projectKeywords.contains("ethics")) {
            return "ethics";
        }
        return "AI Safety"; // Default for our use case
    }
    
    public String getPrimaryOutcomeKeyword() {
        if (outcomeKeywords.contains("reduction")) {
            return "reduction";
        }
        if (outcomeKeywords.contains("improvement")) {
            return "improvement";
        }
        if (outcomeKeywords.contains("accuracy")) {
            return "accuracy";
        }
        return ""; // Default empty
    }
}