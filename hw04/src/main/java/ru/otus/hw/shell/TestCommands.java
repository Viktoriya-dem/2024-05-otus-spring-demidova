package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Test Commands")
@RequiredArgsConstructor
public class TestCommands {

    private final LoginContext loginContext;

    private final ResultService resultService;

    private final TestService testService;

    private final StudentService studentService;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(String userName, String userLastName) {
        loginContext.login(userName, userLastName);
        return String.format("Добро пожаловать: %s %s", userName, userLastName);
    }

    @ShellMethod(value = "Publish event command", key = {"t", "test"})
    @ShellMethodAvailability(value = "isGetTestCommandAvailable")
    public void getTest() {
        TestResult testResult = testService.executeTestFor(studentService
                .determineCurrentStudent(loginContext.getUserName(), loginContext.getUserLastName()));
        resultService.showResult(testResult);
    }

    private Availability isGetTestCommandAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Сначала залогиньтесь");
    }

}
