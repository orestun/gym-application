package org.epam.gymapplication.domain.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.epam.gymapplication.domain.dao.TrainingDAO;
import org.epam.gymapplication.domain.dto.TraineeTrainingCriteriaDTO;
import org.epam.gymapplication.domain.dto.TrainerTrainingCriteriaDTO;
import org.epam.gymapplication.domain.model.Training;
import org.epam.gymapplication.domain.model.TrainingType;
import org.epam.gymapplication.repository.TrainingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class TrainingDAOImpl implements TrainingDAO {

    private final TrainingRepository trainingRepository;
    private final EntityManager entityManager;

    public TrainingDAOImpl(TrainingRepository trainingRepository, EntityManager entityManager) {
        this.trainingRepository = trainingRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void addTraining(Training training) {
        trainingRepository.save(training);
    }

    @Override
    public Set<TrainingType> getAllTrainingTypes() {
        return trainingRepository.getAllTrainingTypes();
    }

    @Override
    public List<Training> getTraineeTrainingByCriteria(TraineeTrainingCriteriaDTO criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = getPredicatesForTraineeRequest(criteria, cb, root);

        query.where(cb.and(predicates.toArray(Predicate[]::new)));
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Training> getTrainerTrainingByCriteria(TrainerTrainingCriteriaDTO criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = getPredicatesForTrainerRequest(criteria, cb, root);

        query.where(cb.and(predicates.toArray(Predicate[]::new)));
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    private List<Predicate> getPredicatesForTraineeRequest(TraineeTrainingCriteriaDTO criteria, CriteriaBuilder cb, Root<Training> root){
        Join<?, ?> traineeTrainingJoin = joinTables(root, "trainee");
        Join<?, ?> trainerTrainingJoin = joinTables(root, "trainer");
        Join<?, ?> traineeUserJoin = joinTables(traineeTrainingJoin, "user");
        Join<?, ?> trainerUserJoin = joinTables(trainerTrainingJoin, "user");
        Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");

        List<Predicate> predicates = new ArrayList<>();

        addTraineeUsernameEqualPredicate(criteria.getUsername(), cb, traineeUserJoin, predicates);
        addDatePredicates(criteria.getPeriodFrom(), criteria.getPeriodTo(), cb, root, predicates);
        addTrainerNamePredicate(criteria.getTrainerName(), cb, predicates, trainerUserJoin);
        addTrainingTypePredicate(criteria.getTrainingType(), cb, predicates, trainingTypeJoin);

        return predicates;
    }

    private List<Predicate> getPredicatesForTrainerRequest(TrainerTrainingCriteriaDTO criteria, CriteriaBuilder cb, Root<Training> root){
        Join<?, ?> traineeTrainingJoin = joinTables(root, "trainee");
        Join<?, ?> trainerTrainingJoin = joinTables(root, "trainer");
        Join<?, ?> traineeUserJoin = joinTables(traineeTrainingJoin, "user");
        Join<?, ?> trainerUserJoin = joinTables(trainerTrainingJoin, "user");

        List<Predicate> predicates = new ArrayList<>();

        addTrainerUsernameEqualPredicate(criteria.getUsername(), cb, trainerUserJoin, predicates);
        addDatePredicates(criteria.getPeriodFrom(), criteria.getPeriodTo(), cb, root, predicates);
        addTraineeNamePredicate(criteria.getTraineeName(), cb, predicates, traineeUserJoin);

        return predicates;
    }

    private Join<?, ?> joinTables(From<?, ?> table, String joinTableName){
        return table.join(joinTableName);
    }

    private void addTraineeUsernameEqualPredicate(String username, CriteriaBuilder cb, Join<?,?> traineeUserJoin, List<Predicate> predicates){
        Predicate usernameEqualPredicate = cb.equal(traineeUserJoin.get("username"), username);
        predicates.add(usernameEqualPredicate);
    }

    private void addTrainerUsernameEqualPredicate(String username, CriteriaBuilder cb, Join<?,?> trainerUserJoin, List<Predicate> predicates){
        Predicate usernameEqualPredicate = cb.equal(trainerUserJoin.get("username"), username);
        predicates.add(usernameEqualPredicate);
    }

    private void addDatePredicates(Date periodFrom, Date periodTo, CriteriaBuilder cb, Root<?> root, List<Predicate> predicates){
        if (periodFrom != null) {
            Predicate datePeriodFromPredicate = cb.greaterThan(root.get("trainingDate"), periodFrom);
            predicates.add(datePeriodFromPredicate);
        }
        if (periodTo != null) {
            Predicate datePeriodToPredicate = cb.lessThan(root.get("trainingDate"), periodTo);
            predicates.add(datePeriodToPredicate);
        }
    }

    private void addTrainerNamePredicate(String name, CriteriaBuilder cb, List<Predicate> predicates, Join<?, ?> trainerUserJoin) {
        if (name != null && !name.isEmpty()) {
            Predicate trainerNamePredicate = cb.like(trainerUserJoin.get("firthName"), "%" + name + "%");
            predicates.add(trainerNamePredicate);
        }
    }

    private void addTraineeNamePredicate(String name, CriteriaBuilder cb, List<Predicate> predicates, Join<?,?> traineeUserJoin) {
        if (name != null && !name.isEmpty()) {
            Predicate traineeNamePredicate = cb.like(traineeUserJoin.get("firthName"), "%" + name + "%");
            predicates.add(traineeNamePredicate);
        }
    }

    private void addTrainingTypePredicate(String typeName, CriteriaBuilder cb, List<Predicate> predicates, Join<?, ?> trainingTypeJoin) {
        if (typeName != null && !typeName.isEmpty()) {
            Predicate trainingTypePredicate = cb.like(trainingTypeJoin.get("trainingTypeName"), "%" + typeName + "%");
            predicates.add(trainingTypePredicate);
        }
    }
}
