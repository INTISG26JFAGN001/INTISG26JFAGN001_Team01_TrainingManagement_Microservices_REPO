# Project Evaluation Service (PES) - Training Management System

Base URL - http://localhost:8093

## Roles and Access
1. ADMIN - Full CRUD access
2. TRAINER - Create, Read, Update
3. TECH_LEAD - Read
4. COACH - Read
5. ASSOCIATE - Read

## API Endpoints

### Review APIs
1. Create review
   POST /api/reviews/project/{projectId}
   Access: SCRUM_LEAD, TECH_LEAD
2. Get review by project id 
   GET /api/reviews/project/{projectId}/all
   Access: ALL ROLES
3. Get review by ID
   GET /api/reviews/{reviewId}
   Access: 'ADMIN', 'TRAINER', 'TECH_LEAD', 'SCRUM_LEAD'
4. Update review
   PUT /api/reviews/{reviewId}
   Access: SCRUM_LEAD, TECH_LEAD
5. Delete review
   DELETE /api/review/{reviewId}
   Access: ADMIN

### Project APIs
1. Create Project
   POST /api/projects/submitProject
   Access: ALL ROLES
2. Get all projects
   GET /api/projects/getProjects
   Access: ALL ROLES
3. Get Project by ID
   GET /api/projects/{projectId}
   Access: ALL ROLES
4. Update Project
   PUT /api/projects/update/{projectId}
   Access: ALL ROLES
5. Delete Project
   DELETE /api/projects/delete/{projectId}
   Access: ALL ROLES

### Evaluation APIs
1. Get Evaluation by ID
   GET /api/evaluations/{evaluationId}
   Access: ADMIN, TECH_LEAD, TRAINER
2. Create evaluation for batch
   Post /api/evaluations/batch/{batchId}/calculate
   Access: ADMIN, TECH_LEAD, TRAINER
3. Get evaluation by batch id and associate id
   GET /api/evaluations/batch/{batchId}/associate/{associateId}
   Access: ALL ROLES
