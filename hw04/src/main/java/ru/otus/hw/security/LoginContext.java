package ru.otus.hw.security;

public interface LoginContext {
    void login(String userName, String userLastName);

    String getUserName();

    String getUserLastName();

    boolean isUserLoggedIn();
}
