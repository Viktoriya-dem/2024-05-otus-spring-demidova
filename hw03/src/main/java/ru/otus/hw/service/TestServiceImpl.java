package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

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
            if (question.answers() != null) {
                ioService.printFormattedLine(question.answers().stream()
                        .map(Answer::text).collect(Collectors.joining("\n")));
                int answer = ioService.readIntForRangeLocalized(1, 3, "TestService.error.message");
                isAnswerValid = question.answers().get(answer - 1).isCorrect();
                testResult.applyAnswer(question, isAnswerValid);
            }
        }
        return testResult;
    }

}
