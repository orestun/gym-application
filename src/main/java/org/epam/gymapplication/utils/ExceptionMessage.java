package org.epam.gymapplication.utils;

public class ExceptionMessage {

    public static String traineeNotFoundByUsername(String username){
        return String.format("Trainee '%s' are not exists", username);
    }

    public static String trainerNotFoundByUsername(String username){
        return String.format("Trainer '%s' are not exists", username);
    }

    public static String userNotFoundByUsername(String username){
        return String.format("User '%s' are not exists", username);
    }

    public static String jwtTokenExpired(){
        return "Jwt token expired";
    }

    public static String badAuthenticationData(){
        return "Bad input credentials";
    }

    public static String bruceForceProtection(){
        return "Too many affords. Try again in 5 minutes";
    }
}
