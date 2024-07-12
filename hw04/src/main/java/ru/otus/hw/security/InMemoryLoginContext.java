package ru.otus.hw.security;

import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class InMemoryLoginContext implements LoginContext {

    private String userName;

    private String userLastName;

    @Override
    public void login(String userName, String userLastName) {
        this.userName = userName;
        this.userLastName = userLastName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getUserLastName() {
        return userLastName;
    }

    @Override
    public boolean isUserLoggedIn() {

        return nonNull(userName) && nonNull(userLastName);
    }
}
