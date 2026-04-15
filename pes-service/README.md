# Project Evaluation Service (PES) - Training Management System

**Base URL:** `http://localhost:8093`

---

## Roles and Access
- **ADMIN** → Full CRUD access
- **TRAINER** → Create, Read, Update
- **TECH_LEAD** → Read
- **COACH** → Read
- **ASSOCIATE** → Read

---

## API Endpoints

###  Review APIs
1. **Create review**  
   `POST /api/reviews/project/{projectId}`  
   Access: `SCRUM_LEAD, TECH_LEAD`

2. **Get reviews by project ID**  
   `GET /api/reviews/project/{projectId}/all`  
   Access: `ALL ROLES`

3. **Get review by ID**  
   `GET /api/reviews/{reviewId}`  
   Access: `ADMIN, TRAINER, TECH_LEAD, SCRUM_LEAD`

4. **Update review**  
   `PUT /api/reviews/{reviewId}`  
   Access: `SCRUM_LEAD, TECH_LEAD`

5. **Delete review**  
   `DELETE /api/review/{reviewId}`  
   Access: `ADMIN`

---

###  Project APIs
1. **Create project**  
   `POST /api/projects/submitProject`  
   Access: `ALL ROLES`

2. **Get all projects**  
   `GET /api/projects/getProjects`  
   Access: `ALL ROLES`

3. **Get project by ID**  
   `GET /api/projects/{projectId}`  
   Access: `ALL ROLES`

4. **Update project**  
   `PUT /api/projects/update/{projectId}`  
   Access: `ALL ROLES`

5. **Delete project**  
   `DELETE /api/projects/delete/{projectId}`  
   Access: `ALL ROLES`

---

###  Evaluation APIs
1. **Get evaluation by ID**  
   `GET /api/evaluations/{evaluationId}`  
   Access: `ADMIN, TECH_LEAD, TRAINER`

2. **Create evaluation for batch**  
   `POST /api/evaluations/batch/{batchId}/calculate`  
   Access: `ADMIN, TECH_LEAD, TRAINER`

3. **Get evaluation by batch ID and associate ID**  
   `GET /api/evaluations/batch/{batchId}/associate/{associateId}`  
   Access: `ALL ROLES`

---

###  Interview Evaluation APIs
1. **Submit interview evaluation**  
   `POST /interview-evaluations`  
   Access: `TRAINER`

2. **Get evaluation by assessment and associate**  
   `GET /interview-evaluations/assessment/{assessmentId}/associate/{associateId}`  
   Access: `TRAINER, ASSOCIATE`

3. **Get all evaluations by assessment**  
   `GET /interview-evaluations/assessment/{assessmentId}`  
   Access: `TRAINER`

4. **Get all evaluations by associate**  
   `GET /interview-evaluations/associate/{associateId}`  
   Access: `TRAINER, ASSOCIATE`