package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream is = Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream(fileNameProvider.getTestFileName()))) {

            List<QuestionDto> beans = new CsvToBeanBuilder<QuestionDto>(
                    new InputStreamReader(is))
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();

            List<Question> questions = beans.stream().map(QuestionDto::toDomainObject).toList();

            return questions;
        } catch (Exception e) {
            throw new QuestionReadException("Can't read file", e);
        }
    }
}
