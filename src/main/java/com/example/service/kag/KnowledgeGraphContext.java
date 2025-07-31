package com.example.service.kag;

import com.example.entity.Employee;
import com.example.entity.Project;
import com.example.entity.Report;
import com.example.entity.Outcome;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeGraphContext {
    
    private List<Employee> employees = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private List<Report> reports = new ArrayList<>();
    private List<Outcome> outcomes = new ArrayList<>();
    
    // Additional context from complex queries
    private List<Object> projectSummaries = new ArrayList<>();
    private List<Object> outcomeDetails = new ArrayList<>();
    private List<Object> reportDetails = new ArrayList<>();
    
    public boolean hasEmployees() {
        return !employees.isEmpty();
    }
    
    public boolean hasProjects() {
        return !projects.isEmpty();
    }
    
    public boolean hasReports() {
        return !reports.isEmpty();
    }
    
    public boolean hasOutcomes() {
        return !outcomes.isEmpty();
    }
    
    public boolean hasProjectSummaries() {
        return !projectSummaries.isEmpty();
    }
    
    public boolean hasOutcomeDetails() {
        return !outcomeDetails.isEmpty();
    }
    
    public boolean hasReportDetails() {
        return !reportDetails.isEmpty();
    }
    
    public boolean isEmpty() {
        return employees.isEmpty() && projects.isEmpty() && reports.isEmpty() && 
               outcomes.isEmpty() && projectSummaries.isEmpty() && 
               outcomeDetails.isEmpty() && reportDetails.isEmpty();
    }
    
    public int getTotalEntities() {
        return employees.size() + projects.size() + reports.size() + outcomes.size() +
               projectSummaries.size() + outcomeDetails.size() + reportDetails.size();
    }
}