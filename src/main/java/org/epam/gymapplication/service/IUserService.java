package org.epam.gymapplication.service;

public interface IUserService {
    String generateUniqueUsername(String firthName, String lastName);
    char[] generateRandom_10CharsPassword();
}
