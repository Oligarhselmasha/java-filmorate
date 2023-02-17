package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GenresDAOImpl implements GenresDAO{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genres> findFilmsGenres(int id) {
        String sql = "SELECT gl.GENRE_ID, g.GENRE " +
                "FROM GENRES_LINE gl " +
                "JOIN GENRES AS g ON g.GENRE_ID = gl.GENRE_ID " +
                "WHERE gl.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs), id);
    }

    @Override
    public List<Genres> getGenres() {
        String sql = "SELECT * " +
                "FROM GENRES ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs));
    }

    @Override
    public Genres getGenresById(int id) {
        try {
            String sql = "SELECT * " +
                    "FROM GENRES " +
                    "WHERE GENRE_ID = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenres(rs), id).stream().findFirst().get();
        } catch (Exception exception) {
            throw new MissingException("Запрашиваемый жанр отсутствует в базе");
        }
    }

    public Genres makeGenres (ResultSet rs) throws SQLException {
       return Genres.builder().id(rs.getInt("GENRE_ID")).name(rs.getString("GENRE")).build();
    }
}
