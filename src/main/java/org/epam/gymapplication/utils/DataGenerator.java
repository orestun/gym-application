package org.epam.gymapplication.utils;

import com.github.javafaker.Faker;
import org.epam.gymapplication.domain.model.Trainee;
import org.epam.gymapplication.domain.model.Trainer;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DataGenerator {

    private final PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    public DataGenerator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Trainee generateTrainee(){
        User traineeUser = generateUser();
        return Trainee.builder()
                .user(traineeUser)
                .dateOfBirth(faker.date().birthday())
                .address(faker.address().fullAddress()).build();
    }

    public Trainer generateTrainer() {
        Random rand = new Random();

        User trainerUser = generateUser();
        return Trainer.builder()
                .user(trainerUser)
                .specialization(new TrainingType(rand.nextLong(1,5),""))
                .build();
    }

    private User generateUser(){
        String firthName = faker.name().firstName();
        String lastName = faker.name().lastName();
        return User.builder()
                .firthName(firthName)
                .secondName(lastName)
                .password(passwordEncoder.encode("password"))
                .username(firthName + "." + lastName)
                .isActive(true)
                .build();
    }
}
