package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.Application;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
@DisplayName("Метод findAll() должен ")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AppProperties appProperties;

    @Test
    @DisplayName(" не бросать исключение если файл найден")
    public void shouldNotThrowQuestionReadExceptionIfQuestionsFileExists() {
        assertThatCode(() -> questionDao.findAll()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName(" бросать исключение если файл не найден")
    public void shouldThrowQuestionReadExceptionIfQuestionsFileNotExists() {
        Map<String, String> fileNameByLocaleMap = new HashMap<>();
        fileNameByLocaleMap.put("en-US", "questions_not_exist.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleMap);
        assertThatThrownBy(() -> questionDao.findAll()).isInstanceOf(QuestionReadException.class);
        appProperties.setFileNameByLocaleTag(getFileNameByLocaleTestData());
    }

    @Test
    @DisplayName(" вернуть корректный список вопросов")
    public void shouldReturnCorrectQuestionsList() {
        Question question = getTestData();
        assertThat(questionDao.findAll()).isNotEmpty().containsExactly(question);
    }

    private static Question getTestData() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("yes", true));
        answers.add(new Answer("no", false));
        return new Question("Read correctly?", answers);
    }

    private static Map<String, String> getFileNameByLocaleTestData() {
        Map<String, String> fileNameByLocaleMap = new HashMap<>();
        fileNameByLocaleMap.put("en-US", "test_questions.csv");
        fileNameByLocaleMap.put("ru-RU", "test_questions_ru.csv");

        return fileNameByLocaleMap;
    }
}
