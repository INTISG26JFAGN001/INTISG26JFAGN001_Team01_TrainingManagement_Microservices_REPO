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
   `POST /reviews/project/{projectId}`  
   Access: `SCRUM_LEAD, TECH_LEAD`

2. **Get reviews by project ID**  
   `GET /reviews/project/{projectId}/all`  
   Access: `ALL ROLES`

3. **Get review by ID**  
   `GET /reviews/{reviewId}`  
   Access: `ADMIN, TRAINER, TECH_LEAD, SCRUM_LEAD`

4. **Update review**  
   `PUT /reviews/{reviewId}`  
   Access: `SCRUM_LEAD, TECH_LEAD`

5. **Delete review**  
   `DELETE /review/{reviewId}`  
   Access: `ADMIN`

---

###  Project APIs
1. **Create project**  
   `POST /projects/submitProject`  
   Access: `ALL ROLES`

2. **Get all projects**  
   `GET /projects/getProjects`  
   Access: `ALL ROLES`

3. **Get project by ID**  
   `GET /projects/{projectId}`  
   Access: `ALL ROLES`

4. **Update project**  
   `PUT /projects/update/{projectId}`  
   Access: `ALL ROLES`

5. **Delete project**  
   `DELETE /projects/delete/{projectId}`  
   Access: `ALL ROLES`

---

###  Evaluation APIs
1. **Submit evaluation**  
   `POST /evaluations/submitEvaluation`  
   Access: `TRAINER,TECH_LEAD,ADMIN` 
2. **Get evaluation by BATCH ID**  
   `GET /evaluations/batch/{batchId}`  
   Access: `ADMIN, TECH_LEAD, TRAINER`
3. **Create evaluation for batch**  
   `POST /evaluations/batch/{batchId}/calculate`  
   Access: `ADMIN, TECH_LEAD, TRAINER`
4. **Get evaluation by batch ID and associate ID**  
   `GET /evaluations/batch/{batchId}/associate/{associateId}`  
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