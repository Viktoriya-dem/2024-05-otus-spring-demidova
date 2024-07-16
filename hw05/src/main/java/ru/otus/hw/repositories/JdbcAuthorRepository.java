package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcAuthorRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcTemplate.query("select id, full_name from authors ", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return Optional.ofNullable(namedParameterJdbcTemplate.query(
                "select id, full_name from authors a where id = :id", params, new AuthorResultSetExtractor()
        ));
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class AuthorResultSetExtractor implements ResultSetExtractor<Author> {

        @Override
        public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
            Author author = new Author();
            while (rs.next()) {
                long id = rs.getLong("id");
                String fullName = rs.getString("full_name");
                author.setId(id);
                author.setFullName(fullName);
            }
            return author;
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
