package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = false;
            ioService.printLine(question.text());
            if (question.answers().size() != 0) {
                AtomicInteger finalNumber = new AtomicInteger();
                ioService.printFormattedLine(question.answers().stream()
                        .map(e -> String.format("%s %s", finalNumber.incrementAndGet(), e.text()))
                        .collect(Collectors.joining("\n")));
                int answer = ioService.readIntForRange(1, 3, "Введите цифру от 1 до 3");
                isAnswerValid = question.answers().get(answer - 1).isCorrect();
                testResult.applyAnswer(question, isAnswerValid);
            }
        }
        return testResult;
    }
}
