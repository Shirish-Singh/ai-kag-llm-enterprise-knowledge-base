package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.schema.Property;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsRelationship {
    
    @RelationshipId
    private Long id;
    
    @Property
    private String evidenceLevel;
    
    @Property
    private String pageNumbers;
    
    @TargetNode
    private Outcome outcome;
}