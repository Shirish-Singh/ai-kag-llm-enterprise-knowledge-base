package com.example.service.kag;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Citation {
    
    private CitationType type;
    private String content;
    private String sourceDocument;
    private String sourceType;
    private String filePath;
    private String metadata;
    
    public String getFormattedCitation() {
        StringBuilder citation = new StringBuilder();
        
        citation.append(sourceDocument);
        
        if (sourceType != null) {
            citation.append(" (").append(sourceType).append(")");
        }
        
        if (filePath != null) {
            citation.append(" - ").append(filePath);
        }
        
        if (metadata != null) {
            citation.append(" - ").append(metadata);
        }
        
        return citation.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Citation citation = (Citation) o;
        return type == citation.type &&
               Objects.equals(sourceDocument, citation.sourceDocument) &&
               Objects.equals(content, citation.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, sourceDocument, content);
    }
}