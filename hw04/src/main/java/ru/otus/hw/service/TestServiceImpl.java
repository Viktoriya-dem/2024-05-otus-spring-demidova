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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

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
                int answer = ioService.readIntForRangeLocalized(1, question.answers().size(),
                        "TestService.error.message");
                isAnswerValid = question.answers().get(answer - 1).isCorrect();
                testResult.applyAnswer(question, isAnswerValid);
            }
        }
        return testResult;
    }

}
