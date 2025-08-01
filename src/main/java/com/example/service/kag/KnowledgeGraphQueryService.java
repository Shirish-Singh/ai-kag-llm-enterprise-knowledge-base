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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.entity.Employee;
import com.example.entity.Project;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnowledgeGraphQueryService {
    
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ReportRepository reportRepository;
    private final OutcomeRepository outcomeRepository;
    
    public KnowledgeGraphContext queryKnowledgeGraph(QueryEntities entities) {
        log.info("=== KNOWLEDGE GRAPH QUERY START ===");
        log.info("Input entities: {}", entities);
        log.info("Query intent: {}", entities.getQueryIntent());
        log.info("Person names: {}", entities.getPersonNames());
        log.info("Employee keywords: {}", entities.getEmployeeKeywords());
        log.info("Project keywords: {}", entities.getProjectKeywords());
        
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        
        // Query based on intent and entities
        switch (entities.getQueryIntent()) {
            case FIND_PEOPLE_BY_PROJECT:
                log.info("Executing FIND_PEOPLE_BY_PROJECT query");
                context = queryPeopleByProject(entities);
                break;
            case FIND_OUTCOMES:
                log.info("Executing FIND_OUTCOMES query");
                context = queryOutcomes(entities);
                break;
            case FIND_PROJECT_OUTCOMES:
                log.info("Executing FIND_PROJECT_OUTCOMES query");
                context = queryProjectOutcomes(entities);
                break;
            case FIND_REPORTS:
                log.info("Executing FIND_REPORTS query");
                context = queryReports(entities);
                break;
            case COMPREHENSIVE_SEARCH:
            default:
                log.info("Executing COMPREHENSIVE_SEARCH query");
                context = queryComprehensive(entities);
                break;
        }
        
        log.info("=== KNOWLEDGE GRAPH QUERY RESULTS ===");
        log.info("Employees found: {}", context.getEmployees() != null ? context.getEmployees().size() : 0);
        log.info("Projects found: {}", context.getProjects() != null ? context.getProjects().size() : 0);
        log.info("Outcomes found: {}", context.getOutcomes() != null ? context.getOutcomes().size() : 0);
        log.info("Reports found: {}", context.getReports() != null ? context.getReports().size() : 0);
        log.info("Outcome details found: {}", context.getOutcomeDetails() != null ? context.getOutcomeDetails().size() : 0);
        log.info("Report details found: {}", context.getReportDetails() != null ? context.getReportDetails().size() : 0);
        log.info("Project summaries found: {}", context.getProjectSummaries() != null ? context.getProjectSummaries().size() : 0);
        log.info("Total entities in context: {}", context.getTotalEntities());
        log.info("=== KNOWLEDGE GRAPH QUERY END ===");
        
        return context;
    }
    
    private KnowledgeGraphContext queryPeopleByProject(QueryEntities entities) {
        log.info("=== QUERY PEOPLE BY PROJECT START ===");
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        log.info("Primary project category: '{}'", category);
        
        try {
            // Find employees who worked on projects in the specified category
            log.info("Executing Cypher query: MATCH (e:Employee)-[:WORKED_ON]->(p:Project) WHERE p.category CONTAINS '{}' OR p.name CONTAINS '{}' RETURN DISTINCT e", category, category);
            var employees = employeeRepository.findByProjectCategory(category);
            log.info("Found {} employees", employees.size());
            context.setEmployees(employees);
            
            // Get related projects
            log.info("Finding projects by category: '{}'", category);
            var projects = projectRepository.findByCategoryOrNameContaining(category);
            log.info("Found {} projects", projects.size());
            context.setProjects(projects);
            
            // Get outcomes from these projects
            log.info("Finding outcomes by project category: '{}'", category);
            var outcomes = outcomeRepository.findOutcomesByProjectCategory(category);
            log.info("Found {} outcome details", outcomes.size());
            context.setOutcomes(new ArrayList<>());
            context.setOutcomeDetails(outcomes);
            
            // Get supporting reports
            log.info("Finding reports by project category: '{}'", category);
            var reports = reportRepository.findReportsByProjectCategory(category);
            log.info("Found {} report details", reports.size());
            context.setReports(new ArrayList<>());
            context.setReportDetails(reports);
            
        } catch (Exception e) {
            log.error("Error querying people by project: {}", e.getMessage(), e);
        }
        
        log.info("=== QUERY PEOPLE BY PROJECT END ===");
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
        log.info("=== COMPREHENSIVE QUERY START ===");
        KnowledgeGraphContext context = new KnowledgeGraphContext();
        String category = entities.getPrimaryProjectCategory();
        log.info("Primary project category for comprehensive search: '{}'", category);
        log.info("Person names in query: {}", entities.getPersonNames());
        
        try {
            // If we have person names, search specifically for those people
            if (!entities.getPersonNames().isEmpty()) {
                log.info("Person names found - searching specifically for these employees");
                List<Employee> specificEmployees = new ArrayList<>();
                List<Project> specificProjects = new ArrayList<>();
                
                for (String personName : entities.getPersonNames()) {
                    // Try different name formats
                    String[] formats = {
                        personName, // "Carol"
                        personName.substring(0, 1).toUpperCase() + personName.substring(1).toLowerCase(), // "Carol"
                        capitalizeWords(personName) // "Carol Johnson" if input was "carol johnson"
                    };
                    
                    for (String nameFormat : formats) {
                        log.info("Searching for employee with name: '{}'", nameFormat);
                        var employees = employeeRepository.findByName(nameFormat);
                        log.info("Found {} employees with name '{}'", employees.size(), nameFormat);
                        specificEmployees.addAll(employees);
                        
                        // Get projects for this specific employee
                        var projects = employeeRepository.findProjectsByEmployeeName(nameFormat);
                        log.info("Found {} projects for employee '{}'", projects.size(), nameFormat);
                        specificProjects.addAll(projects);
                    }
                    
                    // Also try partial matches for "Carol Johnson"
                    if (personName.contains(" ")) {
                        String fullName = capitalizeWords(personName);
                        log.info("Searching for full name: '{}'", fullName);
                        var employees = employeeRepository.findByName(fullName);
                        log.info("Found {} employees with full name '{}'", employees.size(), fullName);
                        specificEmployees.addAll(employees);
                        
                        var projects = employeeRepository.findProjectsByEmployeeName(fullName);
                        log.info("Found {} projects for full name '{}'", projects.size(), fullName);
                        specificProjects.addAll(projects);
                    }
                }
                
                // Remove duplicates
                context.setEmployees(specificEmployees.stream().distinct().collect(Collectors.toList()));
                context.setProjects(specificProjects.stream().distinct().collect(Collectors.toList()));
                
                log.info("After person-specific search: {} employees, {} projects", 
                    context.getEmployees().size(), context.getProjects().size());
            }
            
            // If no specific results from person names, fall back to category search
            if (context.getEmployees().isEmpty()) {
                log.info("No specific person results, falling back to category search");
                var employees = employeeRepository.findByProjectCategory(category);
                log.info("Found {} employees by category", employees.size());
                context.setEmployees(employees);
                
                var projects = projectRepository.findByCategoryOrNameContaining(category);
                log.info("Found {} projects by category", projects.size());
                context.setProjects(projects);
            }
            
            // Skip project summaries for now due to mapping issue
            log.info("Skipping project summaries due to mapping error");
            context.setProjectSummaries(new ArrayList<>());
            
            log.info("Finding outcome details by category: '{}'", category);
            var outcomeDetails = outcomeRepository.findOutcomeDetails(category, "");
            log.info("Found {} outcome details", outcomeDetails.size());
            context.setOutcomeDetails(outcomeDetails);
            
            log.info("Finding report details by category: '{}'", category);
            var reportDetails = reportRepository.findReportsByProjectCategory(category);
            log.info("Found {} report details", reportDetails.size());
            context.setReportDetails(reportDetails);
            
        } catch (Exception e) {
            log.error("Error in comprehensive query: {}", e.getMessage(), e);
        }
        
        log.info("=== COMPREHENSIVE QUERY END ===");
        return context;
    }
    
    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) return str;
        return Arrays.stream(str.split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
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