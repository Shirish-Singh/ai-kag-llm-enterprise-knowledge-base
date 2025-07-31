package com.example.service.kag;

import com.example.repository.EmployeeRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.ReportRepository;
import com.example.repository.OutcomeRepository;
import com.example.service.nlp.QueryEntities;
import com.example.service.nlp.QueryIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnowledgeGraphQueryService {
    
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ReportRepository reportRepository;
    private final OutcomeRepository outcomeRepository;
    
    public KnowledgeGraphContext queryKnowledgeGraph(QueryEntities entities) {
        log.info("Querying knowledge graph with entities: {}", entities);
        
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        
        // Query based on intent and entities
        switch (entities.getQueryIntent()) {
            case FIND_PEOPLE_BY_PROJECT:
                context = queryPeopleByProject(entities);
                break;
            case FIND_OUTCOMES:
                context = queryOutcomes(entities);
                break;
            case FIND_PROJECT_OUTCOMES:
                context = queryProjectOutcomes(entities);
                break;
            case FIND_REPORTS:
                context = queryReports(entities);
                break;
            case COMPREHENSIVE_SEARCH:
            default:
                context = queryComprehensive(entities);
                break;
        }
        
        log.info("Knowledge graph context retrieved with {} employees, {} projects, {} outcomes, {} reports",
                context.getEmployees().size(), context.getProjects().size(), 
                context.getOutcomes().size(), context.getReports().size());
        
        return context;
    }
    
    private KnowledgeGraphContext queryPeopleByProject(QueryEntities entities) {
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        
        try {
            // Find employees who worked on projects in the specified category
            var employees = employeeRepository.findByProjectCategory(category);
            context.setEmployees(employees);
            
            // Get related projects
            var projects = projectRepository.findByCategoryOrNameContaining(category);
            context.setProjects(projects);
            
            // Get outcomes from these projects
            var outcomes = outcomeRepository.findOutcomesByProjectCategory(category);
            context.setOutcomes(new ArrayList<>());
            context.setOutcomeDetails(outcomes);
            
            // Get supporting reports
            var reports = reportRepository.findReportsByProjectCategory(category);
            context.setReports(new ArrayList<>());
            context.setReportDetails(reports);
            
        } catch (Exception e) {
            log.error("Error querying people by project: {}", e.getMessage());
        }
        
        return context;
    }
    
    private KnowledgeGraphContext queryOutcomes(QueryEntities entities) {
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        String outcomeKeyword = entities.getPrimaryOutcomeKeyword();
        
        try {
            var outcomeDetails = outcomeRepository.findOutcomeDetails(category, outcomeKeyword);
            context.setOutcomeDetails(outcomeDetails);
            
            // Get related projects and reports
            var projects = projectRepository.findProjectsWithOutcomesByCategory(category);
            context.setProjects(projects);
            context.setProjectSummaries(new ArrayList<>());
            
        } catch (Exception e) {
            log.error("Error querying outcomes: {}", e.getMessage());
        }
        
        return context;
    }
    
    private KnowledgeGraphContext queryProjectOutcomes(QueryEntities entities) {
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        
        try {
            // Get comprehensive project summary
            var projectSummaries = projectRepository.findProjectSummaryByCategory(category);
            context.setProjectSummaries(projectSummaries);
            
            // Get detailed outcomes
            var outcomeDetails = outcomeRepository.findOutcomesByProjectCategory(category);
            context.setOutcomeDetails(outcomeDetails);
            
        } catch (Exception e) {
            log.error("Error querying project outcomes: {}", e.getMessage());
        }
        
        return context;
    }
    
    private KnowledgeGraphContext queryReports(QueryEntities entities) {
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        
        try {
            var reportDetails = reportRepository.findReportsByProjectCategory(category);
            context.setReportDetails(reportDetails);
            
            // Get related outcomes that these reports document
            var outcomes = outcomeRepository.findOutcomesByProjectCategory(category);
            context.setOutcomeDetails(outcomes);
            
        } catch (Exception e) {
            log.error("Error querying reports: {}", e.getMessage());
        }
        
        return context;
    }
    
    private KnowledgeGraphContext queryComprehensive(QueryEntities entities) {
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        
        try {
            // Get all relevant data for comprehensive response
            var employees = employeeRepository.findByProjectCategory(category);
            context.setEmployees(employees);
            
            var projects = projectRepository.findByCategoryOrNameContaining(category);
            context.setProjects(projects);
            
            var projectSummaries = projectRepository.findProjectSummaryByCategory(category);
            context.setProjectSummaries(projectSummaries);
            
            var outcomeDetails = outcomeRepository.findOutcomeDetails(category, "");
            context.setOutcomeDetails(outcomeDetails);
            
            var reportDetails = reportRepository.findReportsByProjectCategory(category);
            context.setReportDetails(reportDetails);
            
        } catch (Exception e) {
            log.error("Error in comprehensive query: {}", e.getMessage());
        }
        
        return context;
    }
    
    public String formatContextForLLM(KnowledgeGraphContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("KNOWLEDGE GRAPH CONTEXT:\n\n");
        
        // Format employees
        if (!context.getEmployees().isEmpty()) {
            sb.append("EMPLOYEES:\n");
            context.getEmployees().forEach(emp -> {
                sb.append(String.format("- %s (%s) - %s, %s\n", 
                    emp.getName(), emp.getRole(), emp.getDepartment(), emp.getEmail()));
            });
            sb.append("\n");
        }
        
        // Format projects
        if (!context.getProjects().isEmpty()) {
            sb.append("PROJECTS:\n");
            context.getProjects().forEach(proj -> {
                sb.append(String.format("- %s: %s (Status: %s)\n", 
                    proj.getName(), proj.getDescription(), proj.getStatus()));
            });
            sb.append("\n");
        }
        
        // Format project summaries
        if (!context.getProjectSummaries().isEmpty()) {
            sb.append("PROJECT SUMMARIES:\n");
            context.getProjectSummaries().forEach(summary -> {
                sb.append(String.format("- %s\n", summary.toString()));
            });
            sb.append("\n");
        }
        
        // Format outcome details
        if (!context.getOutcomeDetails().isEmpty()) {
            sb.append("OUTCOMES:\n");
            context.getOutcomeDetails().forEach(outcome -> {
                sb.append(String.format("- %s\n", outcome.toString()));
            });
            sb.append("\n");
        }
        
        // Format report details
        if (!context.getReportDetails().isEmpty()) {
            sb.append("SUPPORTING REPORTS:\n");
            context.getReportDetails().forEach(report -> {
                sb.append(String.format("- %s\n", report.toString()));
            });
            sb.append("\n");
        }
        
        return sb.toString();
    }
}