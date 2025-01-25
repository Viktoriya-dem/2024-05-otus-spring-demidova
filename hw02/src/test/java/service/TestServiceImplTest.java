package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Метод executeTestFor() должен ")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    private TestService testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    @DisplayName(" вернуть корректный результат теста")
    public void shouldReturnCorrectTestResult() {
        given(questionDao.findAll()).willReturn(getQuestionTestData());
        given(ioService.readIntForRange(1, 2, "Введите цифру от 1 до 2")).willReturn(1);

        TestResult testResult = testService.executeTestFor(getStudentTestData());
        assertThat(testResult).isEqualTo(getResultTestData());
    }

    @Test
    @DisplayName(" засчитать правильный ответ")
    public void shouldCountCorrectAnswer() {
        given(questionDao.findAll()).willReturn(getQuestionTestData());
        given(ioService.readIntForRange(1, 2, "Введите цифру от 1 до 2")).willReturn(1);

        TestResult testResult = testService.executeTestFor(getStudentTestData());

        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }

    @Test
    @DisplayName(" не засчитать неправильный ответ")
    public void shouldNotCountCorrectAnswer() {
        given(questionDao.findAll()).willReturn(getQuestionTestData());
        given(ioService.readIntForRange(1,  2, "Введите цифру от 1 до 2")).willReturn(2);

        TestResult testResult = testService.executeTestFor(getStudentTestData());
        assertThat(testResult.getRightAnswersCount()).isEqualTo(0);
    }

    private static List<Question> getQuestionTestData() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("yes", true));
        answers.add(new Answer("no", false));
        Question question = new Question("Read correctly?", answers);

        return List.of(question);
    }

    private static Student getStudentTestData() {
        return new Student("Ivan", "Ivanov");
    }

    private static TestResult getResultTestData() {
        Student student = getStudentTestData();
        TestResult testResult = new TestResult(student);
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("yes", true));
        answers.add(new Answer("no", false));
        Question question = new Question("Read correctly?", answers);
        testResult.applyAnswer(question, true);

        return testResult;
    }
}
