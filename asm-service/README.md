### 1. Core Assessment (General)
These endpoints work across both Quizzes and Interviews. Use these for general listings and metadata updates.

| Method   | Endpoint                                       | Authorized Roles     |
|:---------|:-----------------------------------------------|:---------------------|
| `GET`    | `/assessments`                                 | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/batch/{batchId}`                 | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/type/{type}`                     | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/status/{status}`                 | `TRAINER`            |
| `GET`    | `/assessments/batch/{batchId}/type/{type}`     | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/batch/{batchId}/status/{status}` | `TRAINER`            |
| `PATCH`  | `/assessments/{assessmentId}`                  | `TRAINER`            |
| `DELETE` | `/assessments/{assessmentId}`                  | `TRAINER`            |

### 2. Quiz (MCQ & Auto-Grading)
| Method   | Endpoint                                                   | Authorized Roles     |
|:---------|:-----------------------------------------------------------|:---------------------|
| `POST`   | `/assessments/quiz`                                        | `TRAINER`            |
| `GET`    | `/assessments/quiz/{quizId}`                               | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/quiz/batch/{batchId}`                        | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/quiz/batch/{batchId}/status/{status}`        | `TRAINER`            |
| `POST`   | `/assessments/quiz/{quizId}/attempt`                       | `ASSOCIATE`          |
| `GET`    | `/assessments/quiz/{quizId}/attempts/{associateId}/result` | `TRAINER, ASSOCIATE` |
| `GET`    | `/assessments/quiz/{quizId}/attempts`                      | `TRAINER`            |

### 3. Interview (Manual Grading)
Handles Interim and Final evaluations. Note: Scores are fetched live from the PES service.

| Method | Endpoint                                                       | Authorized Roles     |
|:-------|:---------------------------------------------------------------|:---------------------|
| `POST` | `/assessments/interview`                                       | `TRAINER`            |
| `GET`  | `/assessments/interview/{interviewId}`                         | `TRAINER, ASSOCIATE` |
| `GET`  | `/assessments/interview/batch/{batchId}`                       | `TRAINER, ASSOCIATE` |
| `GET`  | `/assessments/interview/batch/{batchId}/category/{category}`   | `TRAINER, ASSOCIATE` |
| `POST` | `/assessments/interview/{interviewId}/publish`                 | `TRAINER`            |
| `GET`  | `/assessments/interview/{interviewId}/results`                 | `TRAINER`            |
| `GET`  | `/assessments/interview/{interviewId}/associate/{associateId}` | `TRAINER, ASSOCIATE` |

### 4. Rubric (Evaluation Logic)
Critical for Interviews. Weights must sum to **exactly 100%**.

| Method   | Endpoint                                           | Authorized Roles |
|:---------|:---------------------------------------------------|:-----------------|
| `POST`   | `/assessments/{assessmentId}/rubrics`              | `TRAINER`        |
| `GET`    | `/assessments/{assessmentId}/rubrics`              | `ANY`            |
| `GET`    | `/assessments/{assessmentId}/rubrics/total-weight` | `TRAINER`        |
| `DELETE` | `/assessments/{assessmentId}/rubrics/{rubricId}`   | `TRAINER`        |
