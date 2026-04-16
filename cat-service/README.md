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
   POST /technologies
   Access: ADMIN, TECH_LEAD
2. Get all technologies
   GET /technologies
   Access: ALL ROLES
3. Get technology by ID
   GET /technologies/{id}
   Access: ALL ROLES
4. Update technology
   PUT /technologies/{id}
   Access: ADMIN, TRAINER, TECH_LEAD 
5. Delete technology
   DELETE /technologies/{id}
   Access: ADMIN, TECH_LEAD 

### Course APIs
1. Create Course
   POST /courses
   Access: ADMIN, TECH_LEAD 
2. Get all courses
   GET /courses
   Access: ALL ROLES
3. Get course by ID
   GET /courses/{id}
   Access: ALL ROLES
4. Update course
   PUT /courses/{id}
   Access: ADMIN, TECH_LEAD, TRAINER
5. Delete course
   DELETE /courses/{id}
   Access: ADMIN, TECH_LEAD

### Stage APIs
1. Create stage
   POST /stages
   Access: ADMIN, TECH_LEAD
2. Get all stages
   GET /stages
   Access: ALL ROLES
3. Get stage by ID
   GET /stages/{id}
   Access: ALL ROLES
4. Update stage
   PUT /stages/{id}
   Access: ADMIN, TECH_LEAD, TRAINER
5. Delete stage
   DELETE /stages/{id}
   Access: ADMIN, TECH_LEAD