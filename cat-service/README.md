# Catalog Service (CAT) - Training Management System

Base URL - http://localhost:8081

## Roles and Access
1. ADMIN - Full CRUD access
2. TRAINER - Create, Read, Update
3. TECH_LEAD - Read
4. COACH - Read
5. ASSOCIATE - Read

## API Endpoints

### Technology APIs
1. Create technology 
   POST /api/technologies
   Access: ADMIN, TECH_LEAD
2. Get all technologies
   GET /api/technologies
   Access: ALL ROLES
3. Get technology by ID
   GET /api/technologies/{id}
   Access: ALL ROLES
4. Update technology
   PUT /api/technologies/{id}
   Access: ADMIN, TRAINER, TECH_LEAD 
5. Delete technology
   DELETE /api/technologies
   Access: ADMIN, TECH_LEAD 

### Course APIs
1. Create Course
   POST /api/courses
   Access: ADMIN, TECH_LEAD 
2. Get all courses
   GET /api/courses
   Access: ALL ROLES
3. Get course by ID
   GET /api/courses/{id}
   Access: ALL ROLES
4. Update course
   PUT /api/courses/{id}
   Access: ADMIN, TECH_LEAD, TRAINER
5. Delete course
   DELETE /api/courses
   Access: ADMIN, TECH_LEAD

### Stage APIs
1. Create stage
   POST /api/stages
   Access: ADMIN, TECH_LEAD
2. Get all stages
   GET /api/stages
   Access: ALL ROLES
3. Get stage by ID
   GET /api/stages/{id}
   Access: ALL ROLES
4. Update stage
   PUT /api/stages/{id}
   Access: ADMIN, TECH_LEAD, TRAINER
5. Delete stage
   DELETE /api/stages
   Access: ADMIN, TECH_LEAD