-- Sample data for ASM Service

-- 1. Insert a Quiz
INSERT INTO assessments (assessment_type, title, batch_id, stage_id, due_date, max_score, created_by, status, duration_minutes, passing_marks)
VALUES ('QUIZ', 'Java Core Fundamentals', 101, 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 10, 501, 'PUBLISHED', 30, 6);

-- 2. Insert Questions for the Quiz (ID will be 1)
INSERT INTO quiz_question (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
VALUES (1, 'Which of these is not a primitive type in Java?', 'int', 'boolean', 'String', 'float', 'OPTION_C', 5);

INSERT INTO quiz_question (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, marks)
VALUES (1, 'What is the default value of a local variable?', '0', 'null', 'Not initialized', 'Depends on type', 'OPTION_C', 5);

-- 3. Insert an Interview
INSERT INTO assessments (assessment_type, title, batch_id, stage_id, due_date, max_score, created_by, status, interview_category, scheduled_date_time, evaluator_role)
VALUES ('INTERVIEW', 'Spring Boot Technical Interview', 101, 2, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 100, 501, 'DRAFT', 'INTERIM', '2026-04-20 10:30:00', 'TECH_LEAD');

-- 4. Insert Rubrics for the Interview (ID will be 2)
INSERT INTO rubrics (assessment_id, criteria, weight)
VALUES (2, 'Dependency Injection Concepts', 40);

INSERT INTO rubrics (assessment_id, criteria, weight)
VALUES (2, 'RESTful API Design', 30);

INSERT INTO rubrics (assessment_id, criteria, weight)
VALUES (2, 'Spring Data JPA & Hibernate', 30);

-- 5. Insert a Test Result (Quiz Attempt)
INSERT INTO assessment_results (assessment_id, associate_id, score, result_status, is_submitted, submitted_at, duration_minutes_taken)
VALUES (1, 9001, 10, 'PASS', TRUE, NOW(), 12);

-- 6. Insert Question Answers for the Attempt (Using IDs from above)
INSERT INTO quiz_attempt_answers (attempt_id, question_id, selected_option, is_correct, marks_awarded)
VALUES (1, 1, 'OPTION_C', TRUE, 5);

INSERT INTO quiz_attempt_answers (attempt_id, question_id, selected_option, is_correct, marks_awarded)
VALUES (1, 2, 'OPTION_C', TRUE, 5);