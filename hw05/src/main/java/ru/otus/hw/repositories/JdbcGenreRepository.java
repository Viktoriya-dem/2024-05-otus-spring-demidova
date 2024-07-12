package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcGenreRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return namedParameterJdbcTemplate.query("select g.id, g.name " +
                                                  "from genres g inner join books_genres bg on g.id = bg.genre_id " +
                                                  "group by g.id, g.name " +
                                                  "order by g.name", new JdbcGenreRepository.GenreRowMapper());
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        Map<String, Object> params = Collections.singletonMap("ids", ids);
        return namedParameterJdbcTemplate.query("select id, name " +
                                                  "from genres " +
                                                  "where id in (:ids) " +
                                                  "order by id", params, new JdbcGenreRepository.GenreRowMapper());
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong(1), rs.getString(2));
        }
    }
}
