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
public class Project {
    
    @Id
    private String id;
    
    @Property
    private String name;
    
    @Property
    private String description;
    
    @Property
    private String category;
    
    @Property
    private String startDate;
    
    @Property
    private String endDate;
    
    @Property
    private String status;
    
    @Property
    private Integer budget;

    @Relationship(type = "PRODUCED")
    private Set<ProducedRelationship> producedReports;

    @Relationship(type = "ACHIEVED")
    private Set<AchievedRelationship> achievedOutcomes;
}