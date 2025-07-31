package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;

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
    private LocalDate achievedDate;
    
    @Property
    private String category;
}