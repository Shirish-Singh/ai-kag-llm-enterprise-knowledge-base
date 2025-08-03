# Knowledge Augmented Generation (KAG) System

A comprehensive Spring Boot application implementing **Knowledge Augmented Generation (KAG)** for enterprise knowledge management using Neo4j and LLMs. Unlike traditional RAG systems that rely on vector similarity, KAG leverages structured knowledge graphs to provide precise, relationship-aware responses with full citation tracking.

## 🎯 What is KAG?

**Knowledge Augmented Generation** goes beyond traditional RAG by using structured knowledge graphs instead of just document embeddings. This enables:

- **Relationship-aware queries**: "Who worked with whom on which projects?"
- **Precise entity matching**: Find specific people, projects, and outcomes
- **Rich context retrieval**: Connected data provides comprehensive answers
- **Explainable results**: Full traceability from query to knowledge graph to response

### KAG vs RAG Comparison

| Feature | Traditional RAG | Knowledge Augmented Generation (KAG) |
|---------|----------------|-------------------------------------|
| **Data Structure** | Document chunks + embeddings | Structured knowledge graph |
| **Search Method** | Vector similarity | Entity extraction + graph traversal |
| **Relationships** | Limited context awareness | Rich relationship understanding |
| **Precision** | Semantic similarity based | Exact entity and relationship matching |
| **Explainability** | Limited source tracking | Full citation trail with relationships |

## 🚀 Live Example

**Query:** *"What did Carol Johnson work on?"*

**KAG Process:**
1. **Entity Extraction**: `[PERSON: Carol Johnson]`
2. **Graph Query**: Find Carol → Projects → Outcomes → Reports
3. **Context Building**: 1 employee, 2 projects, 3 outcomes, 2 reports
4. **LLM Response**: Rich answer with specific details and citations

**Response:**
> "Carol Johnson, the AI Ethics Specialist at the company, worked on two major initiatives:
> 
> 1. **AI Ethics Framework** - As Ethics Lead, she established company-wide ethical guidelines now used in 100% of new AI projects
> 2. **AI Safety Blueprint** - Served as Ethics Consultant, contributing to the comprehensive safety framework that achieved 40% bias reduction
> 
> *Sources: AI Ethics Guidelines v2.0, Q3 Safety Assessment Report*"

## 🏗️ System Architecture

```
┌─────────────────┐    ┌─────────────────────────────────────────────┐    ┌─────────────────┐
│   User Query    │───▶│              KAG Pipeline                   │───▶│   Enhanced      │
│  "What did      │    │                                             │    │   Response      │
│ Carol work on?" │    │  1. Entity Extraction (Stanford CoreNLP)   │    │  + Citations    │
└─────────────────┘    │  2. Knowledge Graph Query (Neo4j)          │    └─────────────────┘
                       │  3. Context Enrichment                      │
                       │  4. LLM Generation (Qwen2.5)               │
                       │  5. Citation Addition                       │
                       └─────────────────────────────────────────────┘
                                            │
                                            ▼
                              ┌─────────────────────────┐
                              │    Neo4j Knowledge      │
                              │        Graph            │
                              │                         │
                              │ • Employees & Roles     │
                              │ • Projects & Outcomes   │
                              │ • Reports & Documents   │
                              │ • Rich Relationships    │
                              └─────────────────────────┘
```

### Core Components

- **🧠 KAGService**: Main orchestration service managing the 7-step KAG pipeline
- **🔍 EntityExtractionService**: Stanford CoreNLP-powered entity recognition and intent classification
- **🕸️ KnowledgeGraphQueryService**: Intelligent Neo4j graph querying with person-specific search
- **📊 Repository Layer**: Spring Data Neo4j repositories for entities (Employee, Project, Outcome, Report)
- **📎 CitationService**: Adds source tracking and citations to responses
- **🎯 Entity Model**: Rich domain entities with relationship mappings
- **🌐 REST API**: RESTful endpoints with comprehensive request/response logging

### Technologies

- **Spring Boot 3.3.0** - Application framework with comprehensive logging
- **Neo4j 5.15** - Graph database with Spring Data Neo4j integration
- **Stanford CoreNLP 4.5.4** - Entity extraction and NLP processing
- **Qwen2.5:7B / OpenAI** - Large Language Models for response generation
- **Docker** - Neo4j containerization with APOC plugins
- **Gradle** - Build system with dependency management

## 🚀 Quick Start

### Prerequisites

- **Java 21+** - Required for Spring Boot 3.3.0
- **Docker** - For Neo4j knowledge graph database
- **AI Model** - Either OpenAI API key or Ollama installation

### 1. Start Neo4j Knowledge Graph

```bash
# Start Neo4j container with APOC plugins
docker-compose up -d

# Wait for Neo4j to start (check http://localhost:7474)
# Default credentials: neo4j/password

# Initialize knowledge graph with comprehensive dummy data
chmod +x init-neo4j-data.sh
./init-neo4j-data.sh
```

### 2. Configure AI Provider

**Option A: OpenAI**
```bash
export OPENAI_API_KEY=your_openai_api_key_here
```

**Option B: Ollama (Local - Recommended)**
```bash
# Install and start Ollama
ollama pull qwen2.5:7b
```

### 3. Run the KAG Application

```bash
# Build and run the application
./gradlew bootRun
```

The application will start on **http://localhost:8081**

## 🔍 Testing the KAG System

### Core KAG Queries

Test the system with these working examples:

```bash
# Person-specific queries
curl -X GET "http://localhost:8081/api/kag/query?q=What%20did%20Carol%20Johnson%20work%20on?"

curl -X GET "http://localhost:8081/api/kag/query?q=Who%20worked%20on%20AI%20Safety%20Blueprint?"

curl -X GET "http://localhost:8081/api/kag/query?q=What%20outcomes%20did%20the%20AI%20safety%20team%20achieve?"

# Project-focused queries
curl -X POST "http://localhost:8081/api/kag/query" \
  -H "Content-Type: application/json" \
  -d '{"query": "What AI safety initiatives has our team completed?"}'

# Outcome-focused queries  
curl -X GET "http://localhost:8081/api/kag/query?q=Show%20me%20bias%20reduction%20achievements"
```

### Example Response Structure

```json
{
  "userQuery": "What did Carol Johnson work on?",
  "extractedEntities": {
    "personNames": ["Johnson", "Carol"],
    "queryIntent": "COMPREHENSIVE_SEARCH",
    "employeeKeywords": [],
    "projectKeywords": []
  },
  "knowledgeGraphContext": {
    "employees": 1,
    "projects": 2,
    "outcomes": 3,
    "reports": 2,
    "totalEntities": 8
  },
  "llmResponse": "Carol Johnson, the AI Ethics Specialist...",
  "responseWithCitations": "...[Sources: Ethics Guidelines v2.0]",
  "citations": [...]
}
```

### Swagger Documentation

Interactive API documentation: **http://localhost:8081/swagger-ui.html**

### Example Queries Endpoint

Get pre-configured working queries:
```bash
curl "http://localhost:8081/api/kag/examples"
```

## 📊 Knowledge Graph Schema

### Entity Model

```
Employee {
  id: string (PK)
  name: string
  email: string  
  department: string
  role: string
  joinDate: string
  skills: string[]
}

Project {
  id: string (PK)
  name: string
  description: string
  category: string
  startDate: string
  endDate: string
  status: string
  budget: integer
}

Report {
  id: string (PK)
  title: string
  content: string
  type: string
  date: string
  filePath: string
  summary: string
}

Outcome {
  id: string (PK)
  description: string
  impactLevel: string
  metrics: string
  achievedDate: string
  category: string
}
```

### Relationship Model

```
Employee -[WORKED_ON]-> Project
  • role: string
  • startDate: string  
  • endDate: string
  • hoursContributed: integer

Employee -[AUTHORED]-> Report
  • authorRole: string
  • dateAuthored: string

Project -[ACHIEVED]-> Outcome
  • contributionLevel: string
  • dateAchieved: string

Project -[PRODUCED]-> Report
  • reportType: string
  • dateProduced: string

Report -[DOCUMENTS]-> Outcome
  • evidenceLevel: string
  • pageNumbers: string

Employee -[COLLABORATED_WITH]-> Employee
  • projectId: string
  • collaborationType: string
  • duration: string
```

## 📈 Sample Knowledge Graph Data

### Employees
- **Alice Smith** - Senior AI Researcher (AI Research Dept)
  - Skills: Machine Learning, AI Safety, Deep Learning, Ethics in AI
- **Bob Lee** - AI Safety Engineer (AI Safety Dept)  
  - Skills: AI Safety, Risk Assessment, Compliance, Safety Protocols
- **Carol Johnson** - AI Ethics Specialist (AI Research Dept)
  - Skills: AI Ethics, Policy Development, Bias Detection, Fairness Testing
- **Frank Rodriguez** - Research Manager (AI Research Dept)
  - Skills: Project Management, AI Research, Team Leadership

### Projects
- **AI Safety Blueprint** (2023) - Comprehensive safety framework - Status: Completed - $500K
- **Bias Detection System** (2023) - Automated bias detection system - Status: Completed - $300K  
- **AI Ethics Framework** (2023-2024) - Company-wide ethical guidelines - Status: In Progress - $250K

### Key Outcomes
- **40% reduction** in AI bias incidents across all systems
- **85% accuracy** in automated bias detection  
- **Safety protocols** implemented across 15 AI systems
- **60% reduction** in harmful AI outputs
- **100% compliance** with ethical guidelines for new projects

### Supporting Reports
- **AI Safety Assessment Q3 2023** - Quarterly assessment with metrics
- **Bias Detection Implementation Report** - Technical implementation details
- **AI Ethics Guidelines v2.0** - Updated policy framework
- **AI Safety Blueprint Final Report** - Complete project documentation

## 🔧 Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── Application.java              # Main Spring Boot application
│   │   ├── controller/
│   │   │   ├── KAGController.java        # REST endpoints for KAG queries
│   │   │   └── HelloController.java      # Health check endpoints
│   │   ├── service/
│   │   │   ├── kag/
│   │   │   │   ├── KAGService.java       # Main KAG orchestration service
│   │   │   │   ├── KnowledgeGraphQueryService.java # Neo4j graph querying
│   │   │   │   ├── KnowledgeGraphContext.java # Context data structure
│   │   │   │   ├── CitationService.java  # Source citation management
│   │   │   │   └── KAGResponse.java      # Response data structure
│   │   │   ├── nlp/
│   │   │   │   ├── EntityExtractionService.java # Stanford CoreNLP integration
│   │   │   │   ├── QueryEntities.java    # Extracted entities structure
│   │   │   │   └── QueryIntent.java      # Query intent enumeration
│   │   │   └── ai/
│   │   │       ├── AIService.java        # AI service interface
│   │   │       ├── AbstractAIService.java # Abstract AI service
│   │   │       └── AIServiceImpl.java    # AI service implementation
│   │   ├── repository/
│   │   │   ├── EmployeeRepository.java   # Employee Neo4j repository
│   │   │   ├── ProjectRepository.java    # Project Neo4j repository
│   │   │   ├── OutcomeRepository.java    # Outcome Neo4j repository
│   │   │   └── ReportRepository.java     # Report Neo4j repository
│   │   ├── entity/
│   │   │   ├── Employee.java             # Employee entity with relationships
│   │   │   ├── Project.java              # Project entity with relationships
│   │   │   ├── Report.java               # Report entity with relationships
│   │   │   ├── Outcome.java              # Outcome entity with relationships
│   │   │   └── *Relationship.java        # Relationship entity classes
│   │   └── config/
│   │       ├── ChatModelConfig.java      # AI model configuration
│   │       └── SwaggerConfiguration.java # API documentation config
│   └── resources/
│       └── application.properties        # Application configuration
├── dummy-data.cypher                     # Neo4j knowledge graph initialization
├── knowledge-graph-schema.cypher         # Graph schema definition
├── init-neo4j-data.sh                   # Data initialization script
└── docker-compose.yml                   # Neo4j container configuration
```

## ⚙️ Configuration

### Application Properties

```properties
# Server Configuration
server.port=8081

# Neo4j Configuration
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=password

# AI Model Configuration
spring.ai.ollama.chat.model=qwen2.5:7b
spring.ai.ollama.base-url=http://localhost:11434

# OpenAI Configuration (if using OpenAI)
spring.ai.openai.api-key=${OPENAI_API_KEY}

# Logging Configuration
logging.level.com.example.service.kag=INFO
logging.level.com.example.service.nlp=INFO
```

### Environment Variables

```bash
# Required for OpenAI (if not using Ollama)
export OPENAI_API_KEY=your_openai_api_key_here

# Optional: Neo4j credentials (if different from defaults)
export NEO4J_USERNAME=neo4j
export NEO4J_PASSWORD=your_password
```

## 🔧 Key Implementation Features

### 1. **Smart Entity Extraction**
- Stanford CoreNLP for person name recognition
- Domain-specific keyword extraction
- Query intent classification (FIND_PEOPLE_BY_PROJECT, COMPREHENSIVE_SEARCH, etc.)
- Multiple name format matching ("Carol", "Carol Johnson", "Johnson")

### 2. **Advanced Graph Querying**
- Person-specific search with fallback to category search
- Relationship traversal for comprehensive context
- Optimized Cypher queries for performance
- Error handling with graceful degradation

### 3. **Comprehensive Logging**
- Request/response tracking with unique IDs
- Entity extraction process logging
- Neo4j query execution logging
- Step-by-step KAG pipeline visibility

### 4. **Citation & Source Tracking**
- Full audit trail from query to response
- Source document references
- Knowledge graph provenance
- Explainable AI compliance

### 5. **Error Handling & Resilience**
- LocalDate/String mapping compatibility for Neo4j
- Graceful fallback for failed queries
- Comprehensive exception handling
- Detailed error logging for debugging

## 🚀 Advanced Usage

### Custom Queries

Add new repository methods for specific use cases:

```java
@Query("MATCH (e:Employee)-[:WORKED_ON]->(p:Project) " +
       "WHERE e.name = $employeeName " +
       "RETURN DISTINCT p")
List<Project> findProjectsByEmployeeName(@Param("employeeName") String employeeName);
```

### Extending Entity Types

Add new entities to the knowledge graph:

```java
@Node
public class Skill {
    @Id private String id;
    @Property private String name;
    @Property private String category;
    // ... relationships
}
```

### Custom Intent Classification

Extend the QueryIntent enum for new query types:

```java
public enum QueryIntent {
    FIND_PEOPLE_BY_PROJECT,
    FIND_SKILLS_BY_PERSON,
    FIND_COLLABORATION_NETWORK,
    // ... add your custom intents
}
```

## 🧪 Development & Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport
```

### Development Mode

```bash
# Run with development profile
./gradlew bootRun --args='--spring.profiles.active=dev'

# Enable debug logging
./gradlew bootRun --args='--logging.level.com.example=DEBUG'
```

### Neo4j Browser

Access Neo4j Browser at **http://localhost:7474** to:
- Explore the knowledge graph visually
- Run custom Cypher queries
- Debug data relationships
- Monitor query performance

## 🚀 Production Deployment

### Environment Setup

1. **Scale Neo4j**: Use Neo4j Enterprise or AuraDB for production
2. **Load Balancing**: Deploy multiple application instances
3. **Monitoring**: Add metrics and health checks
4. **Security**: Configure authentication and authorization
5. **Caching**: Add Redis for query result caching

### Performance Optimization

- **Index Creation**: Add Neo4j indexes for frequently queried properties
- **Query Optimization**: Use EXPLAIN/PROFILE for Cypher query tuning
- **Connection Pooling**: Configure optimal Neo4j connection settings
- **LLM Caching**: Cache similar queries to reduce LLM calls

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add comprehensive tests
4. Ensure all existing tests pass
5. Submit a pull request with detailed description

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🔗 Additional Resources

- [Neo4j Documentation](https://neo4j.com/docs/)
- [Spring Data Neo4j](https://spring.io/projects/spring-data-neo4j)
- [Stanford CoreNLP](https://stanfordnlp.github.io/CoreNLP/)
- [Spring AI Documentation](https://spring.io/projects/spring-ai)

---

**Built with ❤️ for better enterprise knowledge management through Knowledge Augmented Generation**