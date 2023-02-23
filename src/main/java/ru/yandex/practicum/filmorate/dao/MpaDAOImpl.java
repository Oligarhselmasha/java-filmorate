package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.ExceptionMessageEnum.BAD_MPA;

@RequiredArgsConstructor
@Component
public class MpaDAOImpl implements MpaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findFilmsMPA(int id) {
        String sql = "SELECT r.rating_id, r.rating_name " +
                "FROM film f " +
                "JOIN rating AS r ON f.rating_id = r.rating_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs), id)
                .stream()
                .findFirst().orElseThrow(() -> new MissingException(BAD_MPA.getException()));
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * " +
                "FROM rating ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public Mpa getMpaById(int id) {

        String sql = "SELECT * " +
                "FROM rating " +
                "WHERE rating_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs), id)
                .stream()
                .findFirst().orElseThrow(() -> new MissingException(BAD_MPA.getException()));
    }

    public Mpa makeMPA(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .build();
    }
}
