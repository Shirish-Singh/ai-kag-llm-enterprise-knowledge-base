package com.example.service.nlp;

public enum QueryIntent {
    FIND_PEOPLE_BY_PROJECT,    // "Who worked on AI safety?"
    FIND_OUTCOMES,             // "What were the outcomes?"
    FIND_PROJECT_OUTCOMES,     // "What outcomes did the AI safety project achieve?"
    FIND_REPORTS,              // "What reports document the outcomes?"
    COMPREHENSIVE_SEARCH       // General search across all entities
}