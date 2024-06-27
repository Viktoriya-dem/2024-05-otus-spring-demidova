package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
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
        appProperties.setTestFileName("questions_not_exist.csv");
        assertThatThrownBy(() -> questionDao.findAll()).isInstanceOf(QuestionReadException.class);
        appProperties.setTestFileName("questions_test.csv");
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
}
