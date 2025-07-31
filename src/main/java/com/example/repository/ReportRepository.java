package com.example.repository;

import com.example.entity.Report;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends Neo4jRepository<Report, String> {
    
    List<Report> findByType(String type);
    
    @Query("MATCH (r:Report) WHERE r.title CONTAINS $keyword OR r.content CONTAINS $keyword RETURN r")
    List<Report> findByTitleOrContentContaining(@Param("keyword") String keyword);
    
    @Query("MATCH (r:Report)-[:DOCUMENTS]->(o:Outcome) " +
           "WHERE o.category CONTAINS $outcomeCategory " +
           "RETURN r")
    List<Report> findByOutcomeCategory(@Param("outcomeCategory") String outcomeCategory);
    
    @Query("MATCH (e:Employee)-[:AUTHORED]->(r:Report) " +
           "WHERE e.id = $employeeId " +
           "RETURN r ORDER BY r.date DESC")
    List<Report> findByAuthor(@Param("employeeId") String employeeId);
    
    @Query("MATCH (p:Project)-[:PRODUCED]->(r:Report) " +
           "WHERE p.category CONTAINS $projectCategory " +
           "RETURN r, p.name AS projectName ORDER BY r.date DESC")
    List<Object> findReportsByProjectCategory(@Param("projectCategory") String projectCategory);
}