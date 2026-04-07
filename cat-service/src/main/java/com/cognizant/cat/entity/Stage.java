package com.cognizant.cat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer ord;
    private String type;

    @ManyToOne
    @JoinColumn(name= "course_id")
    private Course course;
}
