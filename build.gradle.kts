plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework.ai:spring-ai-starter-model-ollama:1.0.0")
    implementation("org.springframework.ai:spring-ai-starter-model-openai:1.0.0")
    
    // Neo4j dependencies for Knowledge Graph
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.neo4j.driver:neo4j-java-driver:5.15.0")
    
    // JSON processing for AI responses
    implementation("com.fasterxml.jackson.core:jackson-databind")
    
    // NLP libraries for entity extraction
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.4")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.4:models")
    
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}