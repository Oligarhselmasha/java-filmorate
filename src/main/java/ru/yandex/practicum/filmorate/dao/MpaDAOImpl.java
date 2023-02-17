package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MpaDAOImpl implements MpaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findFilmsMPA(int id) {
        String sql = "SELECT r.RATING_ID , r.RATING_NAME " +
                "FROM FILM f " +
                "JOIN rating AS r ON f.rating_id = r.rating_id " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs), id).stream().findFirst().get();
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * " +
                "FROM RATING ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public Mpa getMpaById(int id) {
        try {
            String sql = "SELECT * " +
                    "FROM RATING " +
                    "WHERE RATING_ID = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs), id).stream().findFirst().get();
        } catch (Exception exception) {
            throw new MissingException("Запрашиваемый рейтинг отсутствует в базе");
        }
    }

    public Mpa makeMPA(ResultSet rs) throws SQLException {
        return Mpa.builder().id(rs.getInt("RATING_ID")).name(rs.getString("RATING_NAME")).build();
    }
}
