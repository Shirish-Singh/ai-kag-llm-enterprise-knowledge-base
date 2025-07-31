package com.example.service.kag;

import com.example.entity.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CitationService {
    
    private static final Pattern METRIC_PATTERN = Pattern.compile("(\\d+%|\\d+\\.\\d+%|\\d+ [a-zA-Z]+)");
    private static final Pattern OUTCOME_PATTERN = Pattern.compile("(reduced|improved|achieved|implemented|established)\\s+([^.]+)", 
            Pattern.CASE_INSENSITIVE);
    
    public String addCitations(String llmResponse, KnowledgeGraphContext context) {
        StringBuilder responseWithCitations = new StringBuilder(llmResponse);
        List<Citation> addedCitations = new ArrayList<>();
        
        try {
            // Add citations for specific metrics and outcomes mentioned in the response
            addMetricCitations(responseWithCitations, context, addedCitations);
            addOutcomeCitations(responseWithCitations, context, addedCitations);
            addProjectCitations(responseWithCitations, context, addedCitations);
            
            // Add footnote-style citations at the end
            if (!addedCitations.isEmpty()) {
                responseWithCitations.append("\n\n**Sources:**\n");
                for (int i = 0; i < addedCitations.size(); i++) {
                    Citation citation = addedCitations.get(i);
                    responseWithCitations.append(String.format("[%d] %s", i + 1, citation.getFormattedCitation()));
                    responseWithCitations.append("\n");
                }
            }
            
        } catch (Exception e) {
            log.warn("Error adding citations: {}", e.getMessage());
            return llmResponse; // Return original response if citation fails
        }
        
        return responseWithCitations.toString();
    }
    
    private void addMetricCitations(StringBuilder response, KnowledgeGraphContext context, List<Citation> citations) {
        Matcher matcher = METRIC_PATTERN.matcher(response.toString());
        
        while (matcher.find()) {
            String metric = matcher.group(1);
            
            // Find supporting reports for this metric
            context.getReportDetails().forEach(reportObj -> {
                if (reportObj.toString().contains(metric)) {
                    Citation citation = Citation.builder()
                            .type(CitationType.METRIC)
                            .content(metric)
                            .sourceDocument(extractReportTitle(reportObj.toString()))
                            .sourceType("Report")
                            .build();
                    
                    if (!citations.contains(citation)) {
                        citations.add(citation);
                        // Add inline citation marker
                        int citationNumber = citations.size();
                        String replacement = metric + " [" + citationNumber + "]";
                        response.replace(matcher.start(1), matcher.end(1), replacement);
                    }
                }
            });
        }
    }
    
    private void addOutcomeCitations(StringBuilder response, KnowledgeGraphContext context, List<Citation> citations) {
        Matcher matcher = OUTCOME_PATTERN.matcher(response.toString());
        
        while (matcher.find()) {
            String outcome = matcher.group(0);
            
            // Find supporting reports for this outcome
            context.getOutcomeDetails().forEach(outcomeObj -> {
                if (outcomeObj.toString().toLowerCase().contains(outcome.toLowerCase())) {
                    Citation citation = Citation.builder()
                            .type(CitationType.OUTCOME)
                            .content(outcome)
                            .sourceDocument(extractSourceFromOutcome(outcomeObj.toString()))
                            .sourceType("Outcome Documentation")
                            .build();
                    
                    if (!citations.contains(citation)) {
                        citations.add(citation);
                    }
                }
            });
        }
    }
    
    private void addProjectCitations(StringBuilder response, KnowledgeGraphContext context, List<Citation> citations) {
        // Add citations for project names mentioned
        context.getProjects().forEach(project -> {
            if (response.toString().contains(project.getName())) {
                Citation citation = Citation.builder()
                        .type(CitationType.PROJECT)
                        .content(project.getName())
                        .sourceDocument(project.getName() + " Project Documentation")
                        .sourceType("Project")
                        .metadata("Start: " + project.getStartDate() + ", Status: " + project.getStatus())
                        .build();
                
                if (!citations.contains(citation)) {
                    citations.add(citation);
                }
            }
        });
        
        // Add citations for project summaries
        context.getProjectSummaries().forEach(summary -> {
            String summaryStr = summary.toString();
            if (summaryStr.contains("projectName")) {
                Citation citation = Citation.builder()
                        .type(CitationType.PROJECT_SUMMARY)
                        .content("Project Summary")
                        .sourceDocument(extractProjectNameFromSummary(summaryStr))
                        .sourceType("Project Summary")
                        .build();
                
                if (!citations.contains(citation)) {
                    citations.add(citation);
                }
            }
        });
    }
    
    public List<Citation> extractCitations(KnowledgeGraphContext context) {
        List<Citation> citations = new ArrayList<>();
        
        // Extract citations from reports
        context.getReports().forEach(report -> {
            citations.add(Citation.builder()
                    .type(CitationType.REPORT)
                    .content(report.getTitle())
                    .sourceDocument(report.getTitle())
                    .sourceType("Report")
                    .filePath(report.getFilePath())
                    .metadata("Date: " + report.getDate() + ", Type: " + report.getType())
                    .build());
        });
        
        // Extract citations from report details
        context.getReportDetails().forEach(reportDetail -> {
            String title = extractReportTitle(reportDetail.toString());
            if (title != null) {
                citations.add(Citation.builder()
                        .type(CitationType.REPORT)
                        .content(title)
                        .sourceDocument(title)
                        .sourceType("Report")
                        .build());
            }
        });
        
        return citations;
    }
    
    private String extractReportTitle(String reportString) {
        // Extract report title from the toString() representation
        if (reportString.contains("title=")) {
            int start = reportString.indexOf("title=") + 6;
            int end = reportString.indexOf(",", start);
            if (end == -1) end = reportString.indexOf(")", start);
            if (end > start) {
                return reportString.substring(start, end).trim();
            }
        }
        return "Unknown Report";
    }
    
    private String extractSourceFromOutcome(String outcomeString) {
        // Extract source information from outcome details
        if (outcomeString.contains("documentedIn=")) {
            int start = outcomeString.indexOf("documentedIn=") + 13;
            int end = outcomeString.indexOf("]", start);
            if (end > start) {
                return outcomeString.substring(start, end).trim();
            }
        }
        return "Internal Documentation";
    }
    
    private String extractProjectNameFromSummary(String summaryString) {
        if (summaryString.contains("projectName=")) {
            int start = summaryString.indexOf("projectName=") + 12;
            int end = summaryString.indexOf(",", start);
            if (end > start) {
                return summaryString.substring(start, end).trim();
            }
        }
        return "Project Documentation";
    }
}