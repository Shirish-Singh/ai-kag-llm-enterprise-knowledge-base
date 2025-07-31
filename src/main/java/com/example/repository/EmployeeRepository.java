package com.example.repository;

import com.example.entity.Employee;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends Neo4jRepository<Employee, String> {
    
    Optional<Employee> findByEmail(String email);
    
    List<Employee> findByDepartment(String department);
    
    List<Employee> findByRole(String role);
    
    @Query("MATCH (e:Employee) WHERE ANY(skill IN e.skills WHERE skill CONTAINS $skillKeyword) RETURN e")
    List<Employee> findBySkillContaining(@Param("skillKeyword") String skillKeyword);
    
    @Query("MATCH (e:Employee)-[:WORKED_ON]->(p:Project) " +
           "WHERE p.category CONTAINS $category OR p.name CONTAINS $category " +
           "RETURN DISTINCT e")
    List<Employee> findByProjectCategory(@Param("category") String category);
    
    @Query("MATCH (e:Employee)-[:WORKED_ON]->(p:Project)-[:ACHIEVED]->(o:Outcome) " +
           "WHERE p.category CONTAINS $projectCategory " +
           "OPTIONAL MATCH (r:Report)-[:DOCUMENTS]->(o) " +
           "OPTIONAL MATCH (e)-[:COLLABORATED_WITH]->(colleague:Employee) " +
           "WHERE EXISTS((colleague)-[:WORKED_ON]->(p)) " +
           "RETURN e, " +
           "collect(DISTINCT p) AS workedOnProjects, " +
           "collect(DISTINCT o) AS projectOutcomes, " +
           "collect(DISTINCT r) AS supportingReports, " +
           "collect(DISTINCT colleague) AS collaborators")
    List<Employee> findEmployeesWithProjectOutcomes(@Param("projectCategory") String projectCategory);
}