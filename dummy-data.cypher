// Dummy Data for Employee Knowledge Management KAG System
// Scenario: Company with AI safety initiatives and diverse projects

// ==== CREATE EMPLOYEES ====

// AI Safety Team
CREATE (alice:Employee {
    id: "emp001", 
    name: "Alice Smith", 
    email: "alice.smith@company.com",
    department: "AI Research", 
    role: "Senior AI Researcher", 
    joinDate: "2021-03-15",
    skills: ["Machine Learning", "AI Safety", "Deep Learning", "Ethics in AI"]
});

CREATE (bob:Employee {
    id: "emp002", 
    name: "Bob Lee", 
    email: "bob.lee@company.com",
    department: "AI Safety", 
    role: "AI Safety Engineer", 
    joinDate: "2020-08-20",
    skills: ["AI Safety", "Risk Assessment", "Compliance", "Safety Protocols"]
});

CREATE (carol:Employee {
    id: "emp003", 
    name: "Carol Johnson", 
    email: "carol.johnson@company.com",
    department: "AI Research", 
    role: "AI Ethics Specialist", 
    joinDate: "2022-01-10",
    skills: ["AI Ethics", "Policy Development", "Bias Detection", "Fairness Testing"]
});

// Supporting Team Members
CREATE (david:Employee {
    id: "emp004", 
    name: "David Chen", 
    email: "david.chen@company.com",
    department: "Engineering", 
    role: "Senior Software Engineer", 
    joinDate: "2019-11-05",
    skills: ["Software Engineering", "System Architecture", "API Development"]
});

CREATE (eve:Employee {
    id: "emp005", 
    name: "Eve Wilson", 
    email: "eve.wilson@company.com",
    department: "Data Science", 
    role: "Data Scientist", 
    joinDate: "2021-09-12",
    skills: ["Data Analysis", "Statistical Modeling", "Python", "R"]
});

CREATE (frank:Employee {
    id: "emp006", 
    name: "Frank Rodriguez", 
    email: "frank.rodriguez@company.com",
    department: "AI Research", 
    role: "Research Manager", 
    joinDate: "2018-04-03",
    skills: ["Project Management", "AI Research", "Team Leadership"]
});

// ==== CREATE PROJECTS ====

// Main AI Safety Project
CREATE (aiSafetyBlueprint:Project {
    id: "proj001", 
    name: "AI Safety Blueprint", 
    description: "Comprehensive framework for implementing AI safety protocols across all AI systems",
    category: "AI Safety", 
    startDate: "2023-01-15", 
    endDate: "2023-12-15", 
    status: "Completed",
    budget: 500000
});

// Related AI Projects
CREATE (biasDetection:Project {
    id: "proj002", 
    name: "Bias Detection System", 
    description: "Automated system for detecting and mitigating bias in AI models",
    category: "AI Safety", 
    startDate: "2023-03-01", 
    endDate: "2023-11-30", 
    status: "Completed",
    budget: 300000
});

CREATE (ethicsFramework:Project {
    id: "proj003", 
    name: "AI Ethics Framework", 
    description: "Company-wide ethical guidelines and implementation framework for AI development",
    category: "AI Ethics", 
    startDate: "2023-02-01", 
    endDate: "2024-01-31", 
    status: "In Progress",
    budget: 250000
});

// Non-AI Safety Project (for contrast)
CREATE (customerAnalytics:Project {
    id: "proj004", 
    name: "Customer Analytics Platform", 
    description: "Advanced analytics platform for customer behavior analysis",
    category: "Data Analytics", 
    startDate: "2023-04-01", 
    endDate: "2023-10-31", 
    status: "Completed",
    budget: 400000
});

// ==== CREATE REPORTS ====

CREATE (q3SafetyReport:Report {
    id: "rep001", 
    title: "AI Safety Assessment Q3 2023", 
    content: "Comprehensive analysis of AI safety metrics and implementation progress. Key findings include 40% reduction in bias incidents, successful deployment of safety protocols across 15 AI systems, and establishment of continuous monitoring framework.",
    type: "Quarterly Assessment", 
    date: "2023-09-30",
    filePath: "/reports/ai-safety-q3-2023.pdf",
    summary: "AI bias reduced by 40%, safety protocols implemented across 15 systems"
});

CREATE (biasReport:Report {
    id: "rep002", 
    title: "Bias Detection Implementation Report", 
    content: "Technical report on the implementation and effectiveness of automated bias detection systems. Results show 85% accuracy in detecting gender and racial bias, with automated mitigation reducing harmful outputs by 60%.",
    type: "Technical Report", 
    date: "2023-11-15",
    filePath: "/reports/bias-detection-implementation.pdf",
    summary: "85% accuracy in bias detection, 60% reduction in harmful outputs"
});

CREATE (ethicsGuidelines:Report {
    id: "rep003", 
    title: "AI Ethics Guidelines v2.0", 
    content: "Updated comprehensive ethical guidelines for AI development including fairness principles, transparency requirements, and accountability frameworks.",
    type: "Policy Document", 
    date: "2023-06-15",
    filePath: "/policies/ai-ethics-guidelines-v2.pdf",
    summary: "Updated ethical guidelines with fairness, transparency, and accountability frameworks"
});

CREATE (finalBlueprint:Report {
    id: "rep004", 
    title: "AI Safety Blueprint Final Report", 
    content: "Complete documentation of the AI Safety Blueprint project including methodologies, implementation strategies, results, and recommendations for future safety initiatives.",
    type: "Final Report", 
    date: "2023-12-20",
    filePath: "/reports/ai-safety-blueprint-final.pdf",
    summary: "Complete AI Safety Blueprint with methodologies, results, and future recommendations"
});

// ==== CREATE OUTCOMES ====

CREATE (biasReduction:Outcome {
    id: "out001", 
    description: "Reduced AI bias incidents by 40%", 
    impactLevel: "High",
    metrics: "40% reduction in bias-related incidents across all AI systems",
    achievedDate: "2023-08-15",
    category: "Safety Improvement"
});

CREATE (protocolImplementation:Outcome {
    id: "out002", 
    description: "Implemented safety protocols across 15 AI systems", 
    impactLevel: "High",
    metrics: "15 AI systems now have comprehensive safety monitoring",
    achievedDate: "2023-09-30",
    category: "System Enhancement"
});

CREATE (biasDetectionAccuracy:Outcome {
    id: "out003", 
    description: "Achieved 85% accuracy in automated bias detection", 
    impactLevel: "Medium",
    metrics: "85% accuracy rate in detecting gender and racial bias",
    achievedDate: "2023-10-20",
    category: "Technical Achievement"
});

CREATE (harmfulOutputReduction:Outcome {
    id: "out004", 
    description: "60% reduction in harmful AI outputs", 
    impactLevel: "High",
    metrics: "60% decrease in potentially harmful or biased AI responses",
    achievedDate: "2023-11-01",
    category: "Safety Improvement"
});

CREATE (complianceFramework:Outcome {
    id: "out005", 
    description: "Established comprehensive AI ethics compliance framework", 
    impactLevel: "Medium",
    metrics: "100% of new AI projects now follow ethical guidelines",
    achievedDate: "2023-07-01",
    category: "Policy Implementation"
});

// ==== CREATE RELATIONSHIPS ====

// Employee -> Project relationships (WORKED_ON)
MATCH (alice:Employee {id: "emp001"}), (aiSafety:Project {id: "proj001"})
CREATE (alice)-[:WORKED_ON {role: "Lead Researcher", startDate: "2023-01-15", endDate: "2023-12-15", hoursContributed: 800}]->(aiSafety);

MATCH (bob:Employee {id: "emp002"}), (aiSafety:Project {id: "proj001"})
CREATE (bob)-[:WORKED_ON {role: "Safety Engineer", startDate: "2023-01-15", endDate: "2023-12-15", hoursContributed: 750}]->(aiSafety);

MATCH (alice:Employee {id: "emp001"}), (bias:Project {id: "proj002"})
CREATE (alice)-[:WORKED_ON {role: "Technical Lead", startDate: "2023-03-01", endDate: "2023-11-30", hoursContributed: 600}]->(bias);

MATCH (carol:Employee {id: "emp003"}), (ethics:Project {id: "proj003"})
CREATE (carol)-[:WORKED_ON {role: "Ethics Lead", startDate: "2023-02-01", endDate: "2024-01-31", hoursContributed: 900}]->(ethics);

MATCH (carol:Employee {id: "emp003"}), (aiSafety:Project {id: "proj001"})
CREATE (carol)-[:WORKED_ON {role: "Ethics Consultant", startDate: "2023-01-15", endDate: "2023-06-15", hoursContributed: 300}]->(aiSafety);

// Project Management relationships (MANAGED)
MATCH (frank:Employee {id: "emp006"}), (aiSafety:Project {id: "proj001"})
CREATE (frank)-[:MANAGED {startDate: "2023-01-15", endDate: "2023-12-15"}]->(aiSafety);

// Collaboration relationships (COLLABORATED_WITH)
MATCH (alice:Employee {id: "emp001"}), (bob:Employee {id: "emp002"})
CREATE (alice)-[:COLLABORATED_WITH {projectId: "proj001", collaborationType: "Research Partner", duration: "11 months"}]->(bob);

MATCH (alice:Employee {id: "emp001"}), (carol:Employee {id: "emp003"})
CREATE (alice)-[:COLLABORATED_WITH {projectId: "proj001", collaborationType: "Ethics Advisor", duration: "5 months"}]->(carol);

MATCH (bob:Employee {id: "emp002"}), (carol:Employee {id: "emp003"})
CREATE (bob)-[:COLLABORATED_WITH {projectId: "proj001", collaborationType: "Safety Ethics Team", duration: "5 months"}]->(carol);

// Project -> Report relationships (PRODUCED)
MATCH (aiSafety:Project {id: "proj001"}), (q3Report:Report {id: "rep001"})
CREATE (aiSafety)-[:PRODUCED {reportType: "Progress Report", dateProduced: "2023-09-30"}]->(q3Report);

MATCH (aiSafety:Project {id: "proj001"}), (finalReport:Report {id: "rep004"})
CREATE (aiSafety)-[:PRODUCED {reportType: "Final Report", dateProduced: "2023-12-20"}]->(finalReport);

MATCH (bias:Project {id: "proj002"}), (biasReport:Report {id: "rep002"})
CREATE (bias)-[:PRODUCED {reportType: "Technical Report", dateProduced: "2023-11-15"}]->(biasReport);

MATCH (ethics:Project {id: "proj003"}), (ethicsDoc:Report {id: "rep003"})
CREATE (ethics)-[:PRODUCED {reportType: "Policy Document", dateProduced: "2023-06-15"}]->(ethicsDoc);

// Project -> Outcome relationships (ACHIEVED)
MATCH (aiSafety:Project {id: "proj001"}), (biasReduction:Outcome {id: "out001"})
CREATE (aiSafety)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-08-15"}]->(biasReduction);

MATCH (aiSafety:Project {id: "proj001"}), (protocols:Outcome {id: "out002"})
CREATE (aiSafety)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-09-30"}]->(protocols);

MATCH (bias:Project {id: "proj002"}), (detection:Outcome {id: "out003"})
CREATE (bias)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-10-20"}]->(detection);

MATCH (bias:Project {id: "proj002"}), (harmfulReduction:Outcome {id: "out004"})
CREATE (bias)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-11-01"}]->(harmfulReduction);

MATCH (ethics:Project {id: "proj003"}), (compliance:Outcome {id: "out005"})
CREATE (ethics)-[:ACHIEVED {contributionLevel: "Primary", dateAchieved: "2023-07-01"}]->(compliance);

// Employee -> Report relationships (AUTHORED)
MATCH (alice:Employee {id: "emp001"}), (q3Report:Report {id: "rep001"})
CREATE (alice)-[:AUTHORED {authorRole: "Primary Author", dateAuthored: "2023-09-30"}]->(q3Report);

MATCH (bob:Employee {id: "emp002"}), (q3Report:Report {id: "rep001"})
CREATE (bob)-[:AUTHORED {authorRole: "Contributing Author", dateAuthored: "2023-09-30"}]->(q3Report);

MATCH (alice:Employee {id: "emp001"}), (biasReport:Report {id: "rep002"})
CREATE (alice)-[:AUTHORED {authorRole: "Technical Lead Author", dateAuthored: "2023-11-15"}]->(biasReport);

MATCH (carol:Employee {id: "emp003"}), (ethicsDoc:Report {id: "rep003"})
CREATE (carol)-[:AUTHORED {authorRole: "Primary Author", dateAuthored: "2023-06-15"}]->(ethicsDoc);

MATCH (frank:Employee {id: "emp006"}), (finalReport:Report {id: "rep004"})
CREATE (frank)-[:AUTHORED {authorRole: "Project Summary Author", dateAuthored: "2023-12-20"}]->(finalReport);

// Report -> Outcome relationships (DOCUMENTS)
MATCH (q3Report:Report {id: "rep001"}), (biasReduction:Outcome {id: "out001"})
CREATE (q3Report)-[:DOCUMENTS {evidenceLevel: "Primary Evidence", pageNumbers: "15-23"}]->(biasReduction);

MATCH (q3Report:Report {id: "rep001"}), (protocols:Outcome {id: "out002"})
CREATE (q3Report)-[:DOCUMENTS {evidenceLevel: "Primary Evidence", pageNumbers: "8-14"}]->(protocols);

MATCH (biasReport:Report {id: "rep002"}), (detection:Outcome {id: "out003"})
CREATE (biasReport)-[:DOCUMENTS {evidenceLevel: "Primary Evidence", pageNumbers: "5-12"}]->(detection);

MATCH (biasReport:Report {id: "rep002"}), (harmfulReduction:Outcome {id: "out004"})
CREATE (biasReport)-[:DOCUMENTS {evidenceLevel: "Primary Evidence", pageNumbers: "13-18"}]->(harmfulReduction);

MATCH (ethicsDoc:Report {id: "rep003"}), (compliance:Outcome {id: "out005"})
CREATE (ethicsDoc)-[:DOCUMENTS {evidenceLevel: "Policy Documentation", pageNumbers: "1-50"}]->(compliance);