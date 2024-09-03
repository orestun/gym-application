package org.epam.gymapplication.service.impl;

import org.epam.gymapplication.domain.dao.TraineeDAO;
import org.epam.gymapplication.domain.dao.TrainerDAO;
import org.epam.gymapplication.domain.dao.impl.TraineeDAOImpl;
import org.epam.gymapplication.domain.dao.impl.TrainerDAOImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class UserService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    private final String PASSWORD_CHARS_ALPHABET =
            "0123456789" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "/?.,;:'=+-_";

    public UserService(TraineeDAOImpl traineeDAO, TrainerDAOImpl trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public String generateUniqueUsername(String firthName, String lastName){
        String prefix = String.format("%s.%s", firthName, lastName);
        List<String> similarUsernamesPrefix = getUnionTraineeAndTrainerSimilarUsernames(prefix);
        if(similarUsernamesPrefix != null && !similarUsernamesPrefix.isEmpty()){
            return getIncrementedUsername(prefix, similarUsernamesPrefix);
        }
        return prefix;
    }

    public char[] generateRandom_10CharsPassword(){
        Random random = new Random();

        char[] password = new char[10];
        for(int i = 0; i < 10; i++){
            password[i] = PASSWORD_CHARS_ALPHABET.charAt(random.nextInt(PASSWORD_CHARS_ALPHABET.length()));
        }
        return password;
    }

    private String getIncrementedUsername(String prefix, List<String> similarUsernamesPrefix){
        int incrementNumber = similarUsernamesPrefix.stream()
                .map(u -> u.replace(prefix, ""))
                .filter(u -> !u.isEmpty())
                .mapToInt(Integer::valueOf)
                .max().orElse(0);
        incrementNumber++;
        return prefix + incrementNumber;
    }

    private List<String> getTrainerSimilarUsernames(String username){
        return trainerDAO.findAllTrainerUsernames_ByPrefix(username);
    }

    private List<String> getTraineeSimilarUsernames(String username){
        return traineeDAO.findAllTraineeUsernames_ByPrefix(username);
    }

    private List<String> getUnionTraineeAndTrainerSimilarUsernames(String username){
        List<String> traineeSimilarUsernames = getTraineeSimilarUsernames(username);
        List<String> trainerSimilarUsernames = getTrainerSimilarUsernames(username);
        return Stream
                .concat(trainerSimilarUsernames.stream(), traineeSimilarUsernames.stream())
                .toList();
    }
}
