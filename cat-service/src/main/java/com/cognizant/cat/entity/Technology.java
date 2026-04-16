package com.cognizant.cat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.annotation.processing.Generated;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Technology name required")
    private String name;
}