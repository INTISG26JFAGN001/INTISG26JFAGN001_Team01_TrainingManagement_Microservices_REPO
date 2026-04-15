# Training Execution Serivce (TES) - Training Management System

## Endpoints
###  Associate API

| Method | Endpoint | Authorized Roles |
| --- | --- | --- |
| POST | ``/associates/create`` | ADMIN |
| PUT | ``/associates/update`` | ADMIN |
| GET | ``/associates/{userId}`` | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| GET | ``/associates`` | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| GET | ``/associates/batch?id={batchId}`` | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| GET | ``/associates/xp?value={xp}`` | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |


### Batch API

| Method | Endpoint | Description | Authorized Roles |
| --- | --- | --- | --- |
| POST | ``/batches`` | Create a new batch | ADMIN |
| GET | ``/batches`` | Get all batches | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/batches/{id}`` | Get batch by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| DELETE | ``/batches/{id}`` | Delete batch by ID | ADMIN |
| PUT | ``/batches/{id}/status?status={BatchStatus}`` | Update batch status | ADMIN, TRAINER |
| GET | ``/batches/status?status={BatchStatus}`` | Get batches by status | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/batches/course?course_id={courseId}`` | Get batches by course ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/batches/trainer?trainer_id={trainerId}`` | Get batches by trainer ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/batches/{id}/details`` | Get detailed batch info | ADMIN, TRAINER |
| GET | ``/batches/{batchId}/courses`` | Get courses for a batch | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |

### Enrollment API

| Method | Endpoint | Description | Authorized Roles |
| --- | --- | --- | --- |
| POST | ``/enrollment`` | Create a new enrollment | ADMIN |
| GET | ``/enrollment/{id}`` | Get enrollment by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/enrollment`` | Get all enrollments | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/enrollment/batch?id={batchId}`` | Get enrollments by batch ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/enrollment/associate?id={associateId}`` | Get enrollment by associate ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| PUT | ``/enrollment/{id}/status?val={EnrollmentStatus}`` | Update enrollment status | ADMIN |
| GET | ``/enrollment/status?val={EnrollmentStatus}`` | Get enrollments by status | ADMIN, TRAINER, TECH_LEAD, COACH |
| DELETE | ``/enrollment/{id}`` | Delete enrollment by ID | ADMIN |

### Schedule API

| Method | Endpoint | Description | Authorized Roles |
| --- | --- | --- | --- |
| POST | ``/schedule`` | Create a new schedule | ADMIN |
| GET | ``/schedule/{scheduleId}`` | Get schedule by ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/schedule`` | Get all schedules | ADMIN, TRAINER, TECH_LEAD, COACH |
| PUT | ``/schedule/{scheduleId}/session-date?sessionDate={LocalDateTime}`` | Update session date | ADMIN, TRAINER |
| GET | ``/schedule/batch?id={batchId}`` | Get schedules by batch ID | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |

### Trainer API

| Method | Endpoint | Description | Authorized Roles |
| --- | --- | --- | --- |
| POST | ``/trainer`` | Add a new trainer | ADMIN |
| GET | ``/trainer/{trainerId}`` | Get trainer by ID | ADMIN, TRAINER, TECH_LEAD, COACH, ASSOCIATE |
| DELETE | ``/trainer/{trainerId}`` | Delete trainer by ID | ADMIN |
| GET | ``/trainer`` | Get all trainers | ADMIN, TRAINER, TECH_LEAD, COACH |
| GET | ``/trainer/technology?technologyId={id}`` | Get trainers by technology ID | ADMIN, TRAINER, TECH_LEAD, COACH |
| PUT | ``/trainer/{trainerId}/technologies`` | Update trainer’s technology IDs | ADMIN |
| GET | ``/trainer/{trainerId}/technologies`` | Get technologies for a trainer | ADMIN, TRAINER, TECH_LEAD, COACH |
