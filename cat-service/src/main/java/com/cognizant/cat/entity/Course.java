package com.cognizant.cat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String title;
    private Integer durationDays;

    @ManyToOne
    @JoinColumn(name= "technology_id")
    private Technology technology;

}
