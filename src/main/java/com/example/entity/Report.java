package com.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Set;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    @Id
    private String id;
    
    @Property
    private String title;
    
    @Property
    private String content;
    
    @Property
    private String type;
    
    @Property
    private String date;
    
    @Property
    private String filePath;
    
    @Property
    private String summary;

    @Relationship(type = "DOCUMENTS")
    private Set<DocumentsRelationship> documentedOutcomes;
}