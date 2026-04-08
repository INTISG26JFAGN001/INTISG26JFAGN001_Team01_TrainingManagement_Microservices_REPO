package com.cognizant.asm.entity;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("QUIZ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz extends Assessment {

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "passing_marks")
    private Integer passingMarks;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuizQuestion> questions = new ArrayList<>();
}