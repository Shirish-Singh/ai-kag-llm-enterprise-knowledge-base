package com.example.repository;

import com.example.entity.Outcome;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutcomeRepository extends Neo4jRepository<Outcome, String> {
    
    List<Outcome> findByCategory(String category);
    
    List<Outcome> findByImpactLevel(String impactLevel);
    
    @Query("MATCH (o:Outcome) WHERE o.description CONTAINS $keyword RETURN o")
    List<Outcome> findByDescriptionContaining(@Param("keyword") String keyword);
    
    @Query("MATCH (p:Project)-[:ACHIEVED]->(o:Outcome) " +
           "WHERE p.category CONTAINS $projectCategory " +
           "RETURN DISTINCT o")
    List<Outcome> findOutcomesByProjectCategory(@Param("projectCategory") String projectCategory);
    
    @Query("MATCH (o:Outcome) " +
           "WHERE o.category CONTAINS $category OR o.description CONTAINS $keyword " +
           "RETURN o")
    List<Outcome> findOutcomeDetails(@Param("category") String category, @Param("keyword") String keyword);
}