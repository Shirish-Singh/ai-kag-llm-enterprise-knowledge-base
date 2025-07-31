package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkedOnRelationship {
    
    @RelationshipId
    private Long id;
    
    @Property
    private String role;
    
    @Property
    private LocalDate startDate;
    
    @Property
    private LocalDate endDate;
    
    @Property
    private Integer hoursContributed;
    
    @TargetNode
    private Project project;
}