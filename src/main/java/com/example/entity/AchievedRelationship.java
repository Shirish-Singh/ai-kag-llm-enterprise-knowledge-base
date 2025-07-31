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
public class AchievedRelationship {
    
    @RelationshipId
    private Long id;
    
    @Property
    private String contributionLevel;
    
    @Property
    private LocalDate dateAchieved;
    
    @TargetNode
    private Outcome outcome;
}