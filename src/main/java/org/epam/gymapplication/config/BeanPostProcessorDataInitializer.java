package org.epam.gymapplication.config;

import jakarta.annotation.PostConstruct;
import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.epam.gymapplication.utils.DataGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BeanPostProcessorDataInitializer {

    private final TraineeDAOImpl traineeDAO;
    private final TrainerDAOImpl trainerDAO;
    private final DataGenerator generateData;

    @Value("${app.data-generator.trainee.count:10}")
    private int numberOfTraineeForGenerate;

    @Value("${app.data-generator.trainer.count:10}")
    private int numberOfTrainerForGenerate;

    @Value("${app.data-generator.enabled:false}")
    private boolean isGeneratorActivated;

    public BeanPostProcessorDataInitializer(TraineeDAOImpl traineeDAO, TrainerDAOImpl trainerDAO, DataGenerator generateData) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
        this.generateData = generateData;
    }

    @PostConstruct
    public void initializeData() {
        if (isGeneratorActivated) {
            for (int i = 0; i < numberOfTraineeForGenerate; i++) {
                traineeDAO.addTrainee(generateData.generateTrainee());
            }

            for (int i = 0; i < numberOfTrainerForGenerate; i++) {
                trainerDAO.addTrainer(generateData.generateTrainer());
            }
        }
    }
}
