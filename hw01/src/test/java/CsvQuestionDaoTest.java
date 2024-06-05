import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
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

@DisplayName("Метод findAll() должен ")
@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;
    private QuestionDao questionDao;


    @BeforeEach
    void setUp() {
        questionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    @DisplayName(" не бросать исключение если файл найден")
    public void shouldNotThrowQuestionReadExceptionIfQuestionsFileExists() {
        given(fileNameProvider.getTestFileName()).willReturn("questions_test.csv");
        assertThatCode(() -> questionDao.findAll()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName(" бросать исключение если файл не найден")
    public void shouldThrowQuestionReadExceptionIfQuestionsFileNotExists() {
        given(fileNameProvider.getTestFileName()).willReturn("questions_not_exist.csv");
        assertThatThrownBy(() -> questionDao.findAll()).isInstanceOf(QuestionReadException.class);
    }

    @Test
    @DisplayName(" вернуть корректный список вопросов")
    public void shouldReturnCorrectQuestionsList() {
        given(fileNameProvider.getTestFileName()).willReturn("questions_test.csv");
        Question question = getTestData();
        assertThat(questionDao.findAll()).isNotEmpty().containsExactly(question);
    }

    private static Question getTestData() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("1. yes", true));
        answers.add(new Answer("2. no", false));
        return new Question("Read correctly?", answers);
    }
}
