package com.cognizant.asm.enums;

public enum AssessmentStatus {
    DRAFT,       // Created but not yet published
    PUBLISHED,   // Visible to associates
    CLOSED,      // Deadline passed, no more submissions
    ARCHIVED     // Soft-delete / historical record
}
