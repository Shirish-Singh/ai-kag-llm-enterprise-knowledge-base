package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;
import java.util.Set;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    private String id;
    
    @Property
    private String name;
    
    @Property
    private String email;
    
    @Property
    private String department;
    
    @Property
    private String role;
    
    @Property
    private String joinDate;
    
    @Property
    private List<String> skills;

    @Relationship(type = "WORKED_ON")
    private Set<WorkedOnRelationship> workedOnProjects;

    @Relationship(type = "MANAGED")
    private Set<Project> managedProjects;

    @Relationship(type = "COLLABORATED_WITH")
    private Set<CollaborationRelationship> collaborations;

    @Relationship(type = "AUTHORED")
    private Set<AuthoredRelationship> authoredReports;

    @Relationship(type = "REVIEWED")
    private Set<ReviewedRelationship> reviewedReports;
}