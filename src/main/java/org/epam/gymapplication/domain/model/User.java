package org.epam.gymapplication.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String firthName;

    @Column(nullable = false)
    private String secondName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isActive;
}
