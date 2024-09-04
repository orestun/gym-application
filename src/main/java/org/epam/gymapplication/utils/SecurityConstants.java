package org.epam.gymapplication.utils;

public class SecurityConstants {
    public static final int JWT_TOKEN_LIFE_TIME_60_MINUTES_IN_MILLIS = 60*60*1000;
    public static final int MAX_LOGIN_ATTEMPTS_3_FOR_SPECIFIC_TIME = 3;
    public static final int LOCK_TIME_DURATION_5_MIN_IN_MILLIS = 5 * 60 * 1000;
    public static final String PASSWORD_CHARS_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/?.,;:'=+-_";
}
