// Knowledge Graph Schema for Employee Knowledge Management
// Use Case: "Who in our company has worked on AI safety and what were the main outcomes?"

// ==== NODE CONSTRAINTS AND INDEXES ====

// Employee constraints
CREATE CONSTRAINT employee_email IF NOT EXISTS FOR (e:Employee) REQUIRE e.email IS UNIQUE;
CREATE INDEX employee_name IF NOT EXISTS FOR (e:Employee) ON (e.name);
CREATE INDEX employee_department IF NOT EXISTS FOR (e:Employee) ON (e.department);

// Project constraints  
CREATE CONSTRAINT project_id IF NOT EXISTS FOR (p:Project) REQUIRE p.id IS UNIQUE;
CREATE INDEX project_name IF NOT EXISTS FOR (p:Project) ON (p.name);
CREATE INDEX project_category IF NOT EXISTS FOR (p:Project) ON (p.category);

// Report constraints
CREATE CONSTRAINT report_id IF NOT EXISTS FOR (r:Report) REQUIRE r.id IS UNIQUE;
CREATE INDEX report_title IF NOT EXISTS FOR (r:Report) ON (r.title);
CREATE INDEX report_type IF NOT EXISTS FOR (r:Report) ON (r.type);

// Outcome constraints
CREATE CONSTRAINT outcome_id IF NOT EXISTS FOR (o:Outcome) REQUIRE o.id IS UNIQUE;
CREATE INDEX outcome_impact_level IF NOT EXISTS FOR (o:Outcome) ON (o.impactLevel);

// ==== NODE LABELS AND PROPERTIES ====

// Employee Node
// Properties: id, name, email, department, role, joinDate, skills[]
// Example: (:Employee {id: "emp001", name: "Alice Smith", email: "alice@company.com", 
//          department: "AI Research", role: "Senior AI Researcher", joinDate: "2021-03-15", 
//          skills: ["Machine Learning", "AI Safety", "Deep Learning"]})

// Project Node  
// Properties: id, name, description, category, startDate, endDate, status, budget
// Example: (:Project {id: "proj001", name: "AI Safety Blueprint", 
//          description: "Comprehensive framework for AI safety protocols", 
//          category: "AI Safety", startDate: "2023-01-15", endDate: "2023-12-15", 
//          status: "Completed", budget: 500000})

// Report Node
// Properties: id, title, content, type, date, filePath, summary
// Example: (:Report {id: "rep001", title: "AI Safety Assessment Q3 2023", 
//          content: "Detailed analysis of AI safety metrics...", 
//          type: "Quarterly Assessment", date: "2023-09-30", 
//          filePath: "/reports/ai-safety-q3-2023.pdf", 
//          summary: "AI bias reduced by 40%, new safety protocols implemented"})

// Outcome Node
// Properties: id, description, impactLevel, metrics, achievedDate, category
// Example: (:Outcome {id: "out001", description: "Reduced AI bias by 40%", 
//          impactLevel: "High", metrics: "40% reduction in bias incidents", 
//          achievedDate: "2023-08-15", category: "Safety Improvement"})

// ==== RELATIONSHIP TYPES ====

// Employee -> Project relationships
// WORKED_ON: Employee worked on a project
// Properties: role, startDate, endDate, hoursContributed
// Example: (employee)-[:WORKED_ON {role: "Lead Researcher", startDate: "2023-01-15", 
//          endDate: "2023-12-15", hoursContributed: 800}]->(project)

// MANAGED: Employee managed a project  
// Properties: startDate, endDate
// Example: (employee)-[:MANAGED {startDate: "2023-01-15", endDate: "2023-12-15"}]->(project)

// COLLABORATED_WITH: Employee collaborated with another employee
// Properties: projectId, collaborationType, duration
// Example: (employee1)-[:COLLABORATED_WITH {projectId: "proj001", 
//          collaborationType: "Research Partner", duration: "11 months"}]->(employee2)

// Project -> Report relationships
// PRODUCED: Project produced a report
// Properties: reportType, dateProduced
// Example: (project)-[:PRODUCED {reportType: "Final Report", dateProduced: "2023-12-15"}]->(report)

// Project -> Outcome relationships  
// ACHIEVED: Project achieved an outcome
// Properties: contributionLevel, dateAchieved
// Example: (project)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-08-15"}]->(outcome)

// Employee -> Report relationships
// AUTHORED: Employee authored a report
// Properties: authorRole, dateAuthored
// Example: (employee)-[:AUTHORED {authorRole: "Primary Author", dateAuthored: "2023-09-30"}]->(report)

// REVIEWED: Employee reviewed a report
// Properties: reviewDate, reviewStatus
// Example: (employee)-[:REVIEWED {reviewDate: "2023-10-05", reviewStatus: "Approved"}]->(report)

// Report -> Outcome relationships
// DOCUMENTS: Report documents an outcome
// Properties: evidenceLevel, pageNumbers
// Example: (report)-[:DOCUMENTS {evidenceLevel: "Primary Evidence", pageNumbers: "15-23"}]->(outcome)

// ==== SAMPLE QUERIES FOR THE USE CASE ====

// Query 1: Find employees who worked on AI safety projects
// MATCH (e:Employee)-[:WORKED_ON]->(p:Project {category: "AI Safety"})
// RETURN e.name, e.role, p.name, p.description

// Query 2: Find outcomes from AI safety projects with citations
// MATCH (p:Project {category: "AI Safety"})-[:ACHIEVED]->(o:Outcome)
// OPTIONAL MATCH (r:Report)-[:DOCUMENTS]->(o)
// RETURN p.name, o.description, o.metrics, collect(r.title) as sources

// Query 3: Complete KAG query for "Who worked on AI safety and what were the outcomes?"
// MATCH (e:Employee)-[:WORKED_ON {role: role}]->(p:Project)
// WHERE p.category CONTAINS "AI Safety" OR p.name CONTAINS "AI Safety"
// MATCH (p)-[:ACHIEVED]->(o:Outcome)
// OPTIONAL MATCH (r:Report)-[:DOCUMENTS]->(o)
// OPTIONAL MATCH (e)-[:COLLABORATED_WITH]->(colleague:Employee)
// RETURN e.name, e.role, p.name, p.description, 
//        collect(DISTINCT o.description) as outcomes,
//        collect(DISTINCT o.metrics) as metrics,
//        collect(DISTINCT r.title) as reports,
//        collect(DISTINCT colleague.name) as collaborators