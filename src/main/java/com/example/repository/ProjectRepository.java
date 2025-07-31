package com.example.repository;

import com.example.entity.Project;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends Neo4jRepository<Project, String> {
    
    List<Project> findByCategory(String category);
    
    List<Project> findByStatus(String status);
    
    @Query("MATCH (p:Project) WHERE p.name CONTAINS $keyword OR p.description CONTAINS $keyword RETURN p")
    List<Project> findByNameOrDescriptionContaining(@Param("keyword") String keyword);
    
    @Query("MATCH (p:Project) WHERE p.category CONTAINS $category OR p.name CONTAINS $category RETURN p")
    List<Project> findByCategoryOrNameContaining(@Param("category") String category);
    
    @Query("MATCH (p:Project)-[:ACHIEVED]->(o:Outcome) " +
           "WHERE p.category CONTAINS $category " +
           "RETURN p, collect(o) AS outcomes")
    List<Project> findProjectsWithOutcomesByCategory(@Param("category") String category);
    
    @Query("MATCH (e:Employee)-[:WORKED_ON]->(p:Project) " +
           "WHERE p.category CONTAINS $category " +
           "MATCH (p)-[:ACHIEVED]->(o:Outcome) " +
           "OPTIONAL MATCH (r:Report)-[:DOCUMENTS]->(o) " +
           "RETURN p.name AS projectName, " +
           "p.description AS projectDescription, " +
           "collect(DISTINCT e.name) AS teamMembers, " +
           "collect(DISTINCT o.description) AS outcomes, " +
           "collect(DISTINCT o.metrics) AS metrics, " +
           "collect(DISTINCT r.title) AS supportingReports")
    List<Object> findProjectSummaryByCategory(@Param("category") String category);
}