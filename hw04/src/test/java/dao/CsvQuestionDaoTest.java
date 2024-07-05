package dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = CsvQuestionDao.class)
@DisplayName("Метод findAll() должен ")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private AppProperties appProperties;

    @Test
    @DisplayName(" не бросать исключение если файл найден")
    public void shouldNotThrowQuestionReadExceptionIfQuestionsFileExists() {
        given(appProperties.getTestFileName()).willReturn("test_questions.csv");
        assertThatCode(() -> questionDao.findAll()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName(" бросать исключение если файл не найден")
    public void shouldThrowQuestionReadExceptionIfQuestionsFileNotExists() {
        given(appProperties.getTestFileName()).willReturn("questions_not_exist.csv");
        assertThatThrownBy(() -> questionDao.findAll()).isInstanceOf(QuestionReadException.class);
    }

    @Test
    @DisplayName(" вернуть корректный список вопросов")
    public void shouldReturnCorrectQuestionsList() {
        given(appProperties.getTestFileName()).willReturn("test_questions.csv");
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
