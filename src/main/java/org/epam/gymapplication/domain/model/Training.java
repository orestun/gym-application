package org.epam.gymapplication.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Training")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Trainee trainee;

    @ManyToOne
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @ManyToOne
    private TrainingType trainingType;

    @Column(nullable = false)
    private Date trainingDate;

    @Column(nullable = false)
    private double trainingDuration;
}
