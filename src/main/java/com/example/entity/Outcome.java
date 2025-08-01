package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;


@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Outcome {
    
    @Id
    private String id;
    
    @Property
    private String description;
    
    @Property
    private String impactLevel;
    
    @Property
    private String metrics;
    
    @Property
    private String achievedDate;
    
    @Property
    private String category;
}