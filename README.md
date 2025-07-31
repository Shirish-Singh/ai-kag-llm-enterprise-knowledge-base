# Knowledge-Augmented Generation (KAG) System

A comprehensive Spring Boot application implementing Knowledge-Augmented Generation for enterprise knowledge management. This system demonstrates the KAG use case: **"Who in our company has worked on AI safety and what were the main outcomes?"**

## 🎯 KAG Use Case Overview

This project transforms traditional AI responses by grounding them in real enterprise data stored in a knowledge graph. Instead of generating potentially inaccurate information, the system:

1. **Interprets** natural language queries using NLP
2. **Extracts** entities and intent from user questions  
3. **Queries** a Neo4j knowledge graph for relevant context
4. **Augments** LLM prompts with factual company data
5. **Generates** responses with citations and source tracking
6. **Provides** traceable, auditable answers grounded in real data

## 🏗️ Architecture

### Core Components

- **Neo4j Knowledge Graph**: Stores employees, projects, reports, outcomes with rich relationships
- **Entity Extraction Service**: Stanford NLP-powered entity recognition and intent classification
- **Knowledge Graph Query Service**: Intelligent graph querying based on extracted entities
- **KAG Service**: Orchestrates the complete pipeline from query to response
- **Citation Service**: Adds source tracking and citations to responses
- **REST API**: Provides endpoints for KAG queries with Swagger documentation

### Technologies

- **Spring Boot 3.3.0** - Application framework
- **Neo4j 5.15** - Graph database for knowledge storage
- **Stanford CoreNLP 4.5.4** - Entity extraction and NLP processing
- **Spring AI** - LLM integration (OpenAI/Ollama support)
- **Docker** - Neo4j containerization
- **Gradle** - Build system

## 🚀 Quick Start

### Prerequisites

- **Java 17+** - Required for Spring Boot 3.3.0
- **Docker** - For Neo4j knowledge graph database
- **AI Model** - Either OpenAI API key or Ollama installation

### 1. Start Neo4j Knowledge Graph

```bash
# Start Neo4j container with APOC plugins
docker-compose up -d

# Initialize knowledge graph with dummy data
./init-neo4j-data.sh
```

### 2. Configure AI Provider

**Option A: OpenAI**
```bash
export OPENAI_API_KEY=your_openai_api_key_here
```

**Option B: Ollama (Local)**
```bash
ollama pull qwen2.5:7b
```

### 3. Run the KAG Application

```bash
./gradlew bootRun
```

The application will start on http://localhost:8081

## 🔍 Testing the KAG System

### Core KAG Query

Test the main use case with a POST request:

```bash
curl -X POST "http://localhost:8081/api/kag/query" \
  -H "Content-Type: application/json" \
  -d '{"query": "Who in our company has worked on AI safety and what were the main outcomes?"}'
```

### Example Queries

Get pre-configured example queries:
```bash
curl "http://localhost:8081/api/kag/examples"
```

### Swagger Documentation

Interactive API documentation: http://localhost:8081/swagger-ui.html

## 📊 Knowledge Graph Schema

### Entities

- **Employee**: name, role, department, skills, email
- **Project**: name, description, category, dates, status, budget  
- **Report**: title, content, type, date, filePath, summary
- **Outcome**: description, impactLevel, metrics, category

### Relationships

- `WORKED_ON`: Employee → Project (with role, dates, hours)
- `ACHIEVED`: Project → Outcome (with contribution level)
- `PRODUCED`: Project → Report (with report type)
- `DOCUMENTS`: Report → Outcome (with evidence level)
- `AUTHORED`: Employee → Report (with author role)
- `COLLABORATED_WITH`: Employee ↔ Employee (with project context)

## 📈 Sample Data

The system includes comprehensive dummy data:

### Employees
- **Alice Smith** - Senior AI Researcher (AI Research Dept)
- **Bob Lee** - AI Safety Engineer (AI Safety Dept)  
- **Carol Johnson** - AI Ethics Specialist (AI Research Dept)
- **Frank Rodriguez** - Research Manager (AI Research Dept)

### Projects
- **AI Safety Blueprint** - Comprehensive safety framework (2023)
- **Bias Detection System** - Automated bias detection (2023)
- **AI Ethics Framework** - Company-wide ethical guidelines (2023-2024)

### Key Outcomes
- 40% reduction in AI bias incidents
- 85% accuracy in automated bias detection
- Safety protocols implemented across 15 AI systems
- 60% reduction in harmful AI outputs

## Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── Application.java          # Main application class
│   │   ├── config/
│   │   │   ├── ChatModelConfig.java  # AI model configuration
│   │   │   └── SwaggerConfiguration.java
│   │   ├── controller/               # REST controllers
│   │   └── service/
│   │       ├── ai/
│   │       │   ├── AIService.java    # AI service interface
│   │       │   ├── AbstractAIService.java # Abstract AI service
│   │       │   └── AIServiceImpl.java # AI service implementation
│   │       └── HelloService.java
│   └── resources/
│       └── application.properties    # Application configuration
└── test/
    └── java/com/example/
        └── ApplicationTests.java     # Test classes
```

## AI Model Configuration

The application supports multiple AI providers through Spring AI:

### Switching Between AI Models

To use a specific AI model, add the `@Qualifier` annotation to your constructor parameter:

```java
@Autowired
public AIServiceImpl(@Qualifier("openAiModel") ChatModel chatModel) {
    this.chatModel = chatModel;
}
```

Available qualifiers:
- `"openAiModel"` - Uses OpenAI ChatGPT models
- `"ollamaModel"` - Uses Ollama local models (default/primary)

### Configuration Properties

Update `application.properties` to configure your AI providers:

```properties
# OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Ollama Configuration  
spring.ai.ollama.chat.model=qwen2.5:7b
```

## Customization

1. Update the package name from `com.example` to your desired package
2. Rename the main `Application.java` class if needed
3. Add your specific dependencies to `build.gradle.kts`
4. Configure AI providers in `application.properties`
5. Implement your AI service logic by extending `AbstractAIService`
6. Add your business logic to services and controllers