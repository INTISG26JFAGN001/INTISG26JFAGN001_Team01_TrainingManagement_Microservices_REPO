-- 1. Main Assessments table (Parent for Quizzes and Interviews)
CREATE TABLE IF NOT EXISTS assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_type VARCHAR(31) NOT NULL, -- Hibernate Discriminator
    title VARCHAR(200) NOT NULL,
    batch_id BIGINT NOT NULL,
    stage_id BIGINT,
    due_date DATE,
    max_score INT,
    created_by BIGINT,
    status VARCHAR(20) DEFAULT 'DRAFT',

    -- Quiz specific fields
    duration_minutes INT,
    passing_marks INT,

    -- Interview specific fields
    interview_category VARCHAR(20),
    scheduled_date_time DATETIME,
    evaluator_role VARCHAR(20)
);

-- 2. Quiz Questions table
CREATE TABLE IF NOT EXISTS quiz_question (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quiz_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option VARCHAR(10) NOT NULL,
    marks INT DEFAULT 1,
    CONSTRAINT fk_quiz_question_parent FOREIGN KEY (quiz_id) REFERENCES assessments(id) ON DELETE CASCADE
);

-- 3. Rubrics table (Evaluation criteria for Interviews)
CREATE TABLE IF NOT EXISTS rubrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    criteria VARCHAR(255) NOT NULL,
    weight INT NOT NULL,
    CONSTRAINT fk_rubric_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

-- 4. Assessment Results (Quiz Attempts)
CREATE TABLE IF NOT EXISTS assessment_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    associate_id BIGINT NOT NULL,
    score INT,
    result_status VARCHAR(10) NOT NULL,
    is_submitted BOOLEAN DEFAULT FALSE,
    submitted_at DATETIME,
    duration_minutes_taken INT,
    CONSTRAINT uk_quiz_attempt UNIQUE (assessment_id, associate_id),
    CONSTRAINT fk_result_assessment FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

-- 5. Quiz Attempt Answers (Detailed responses for each question)
CREATE TABLE IF NOT EXISTS quiz_attempt_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option VARCHAR(10),
    is_correct BOOLEAN,
    marks_awarded INT,
    CONSTRAINT fk_ans_attempt FOREIGN KEY (attempt_id) REFERENCES assessment_results(id) ON DELETE CASCADE,
    CONSTRAINT fk_ans_question FOREIGN KEY (question_id) REFERENCES quiz_question(id)
);