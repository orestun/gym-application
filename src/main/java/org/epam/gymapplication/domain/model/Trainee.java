package org.epam.gymapplication.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Trainee")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date dateOfBirth;

    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "trainee", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Trainer> trainers;
}
