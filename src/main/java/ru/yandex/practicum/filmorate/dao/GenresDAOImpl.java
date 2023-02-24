package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.ExceptionMessageEnum.BAD_GENRE;

@RequiredArgsConstructor
@Component
public class GenresDAOImpl implements GenresDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genres> findFilmsGenres(int id) {
        String sql = "SELECT gl.genre_id, g.genre_name " +
                "FROM genres_line gl " +
                "JOIN genre AS g ON g.genre_id = gl.genre_id " +
                "WHERE gl.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs), id);
    }

    @Override
    public List<Genres> getGenres() {
        String sql = "SELECT * " +
                "FROM genre ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs));
    }

    @Override
    public Genres getGenresById(int id) {
        String sql = "SELECT * " +
                "FROM genre " +
                "WHERE genre_id = ?";
        return jdbcTemplate.query(
                        sql, (rs, rowNum) -> makeGenres(rs), id
                ).stream()
                .findFirst()
                .orElseThrow(() -> new MissingException(BAD_GENRE.getException()));
    }

    public Genres makeGenres(ResultSet rs) throws SQLException {
        return Genres.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
