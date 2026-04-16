### 1. Core Assessment (General)
These endpoints work across both Quizzes and Interviews. Use these for general listings and metadata updates.

| Method   | Endpoint                                       | Authorized Roles                                |
|:---------|:-----------------------------------------------|:------------------------------------------------|
| `GET`    | `/assessments`                                 | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/batch/{batchId}`                 | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/type/{type}`                     | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/status/{status}`                 | `ADMIN, TRAINER, TECH_LEAD`                     |
| `GET`    | `/assessments/batch/{batchId}/type/{type}`     | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/batch/{batchId}/status/{status}` | `ADMIN, TRAINER, TECH_LEAD`                     |
| `PATCH`  | `/assessments/{assessmentId}`                  | `ADMIN, TRAINER, TECH_LEAD`                     |
| `DELETE` | `/assessments/{assessmentId}`                  | `ADMIN, TRAINER, TECH_LEAD`                     |

### 2. Quiz (MCQ & Auto-Grading)
| Method   | Endpoint                                                   | Authorized Roles                                |
|:---------|:-----------------------------------------------------------|:------------------------------------------------|
| `POST`   | `/assessments/quiz`                                        | `ADMIN, TRAINER, TECH_LEAD`                     |
| `GET`    | `/assessments/quiz/{quizId}`                               | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/quiz/batch/{batchId}`                        | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/quiz/batch/{batchId}/status/{status}`        | `ADMIN, TRAINER, TECH_LEAD`                     |
| `POST`   | `/assessments/quiz/{quizId}/attempt`                       | `ASSOCIATE`                                     |
| `GET`    | `/assessments/quiz/{quizId}/attempts/{associateId}/result` | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE`          |
| `GET`    | `/assessments/quiz/{quizId}/attempts`                      | `ADMIN, TRAINER, TECH_LEAD`                     |

### 3. Interview (Manual Grading)
Handles Interim and Final evaluations. Note: Scores are fetched live from the PES service.

| Method | Endpoint                                                       | Authorized Roles                       |
|:-------|:---------------------------------------------------------------|:---------------------------------------|
| `POST` | `/assessments/interview`                                       | `ADMIN, TRAINER, TECH_LEAD`            |
| `GET`  | `/assessments/interview/{interviewId}`                         | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE` |
| `GET`  | `/assessments/interview/batch/{batchId}`                       | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE` |
| `GET`  | `/assessments/interview/batch/{batchId}/category/{category}`   | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE` |
| `POST` | `/assessments/interview/{interviewId}/publish`                 | `ADMIN, TRAINER, TECH_LEAD`            |
| `GET`  | `/assessments/interview/{interviewId}/results`                 | `ADMIN, TRAINER, TECH_LEAD`            |
| `GET`  | `/assessments/interview/{interviewId}/associate/{associateId}` | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE` |

### 4. Rubric (Evaluation Logic)
Critical for Interviews. Weights must sum to **exactly 100%**.

| Method   | Endpoint                                           | Authorized Roles                       |
|:---------|:---------------------------------------------------|:---------------------------------------|
| `POST`   | `/assessments/{assessmentId}/rubrics`              | `ADMIN, TRAINER, TECH_LEAD`            |
| `GET`    | `/assessments/{assessmentId}/rubrics`              | `ADMIN, TRAINER, TECH_LEAD, ASSOCIATE` |
| `GET`    | `/assessments/{assessmentId}/rubrics/total-weight` | `ADMIN, TRAINER, TECH_LEAD`            |
| `DELETE` | `/assessments/{assessmentId}/rubrics/{rubricId}`   | `ADMIN, TRAINER, TECH_LEAD`            |
