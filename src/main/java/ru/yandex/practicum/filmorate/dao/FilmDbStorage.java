package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingsEnum;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenresDAOImpl genresDAO;
    private final MpaDAOImpl mpaDAO;

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Данные в запросе на добавление нового фильма не соответствуют критериям.");
        }
        String sql = "INSERT INTO Film (Name, " +
                "        Description, " +
                "        Realise_date, " +
                "        Duration, " +
                "        Rating_id) values " +
                "(?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"Film_id"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
                ps.setInt(4, film.getDuration());
                ps.setString(5, film.getMpa().toString());
                return ps;
            }
        }, keyHolder);
        if (film.getGenres() != null) {
            String sqlGenreAdd = "INSERT INTO Genres_line (Film_id, genre_id) " +
                    "values " +
                    "(?, ?)";
            film.getGenres().forEach(g -> jdbcTemplate.update(sqlGenreAdd, keyHolder.getKey().intValue(), g.toString()));
        }
        return getFilmById(keyHolder.getKey().intValue());
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            String sql = "UPDATE FILM  SET Name = ?, Description = ?, Realise_date = ?, Duration = ?, Rating_id = ? WHERE Film_id = ?; ";
            jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    java.sql.Date.valueOf(film.getReleaseDate()),
                    film.getDuration(),
                    film.getMpa().toString(),
                    film.getId());
            if (film.getGenres() == null) {
                return getFilmById(film.getId());
            }
            if (film.getGenres().size() == 0) {
                String sqlDeleteGenres = "delete from Genres_line where FILM_ID = ?";
                jdbcTemplate.update(sqlDeleteGenres, film.getId());
                return getFilmById(film.getId());
            }
            if (film.getGenres() != null) {
                String sqlDeleteGenres = "delete from Genres_line where FILM_ID = ?";
                jdbcTemplate.update(sqlDeleteGenres, film.getId());
                String sqlGenreAdd = "INSERT INTO Genres_line (Film_id, genre_id) " +
                        "values " +
                        "(?, ?)";
                film.getGenres()
                        .stream()
                        .distinct()
                        .collect(Collectors.toList())
                        .forEach(g -> jdbcTemplate.update(sqlGenreAdd, film.getId(), g.toString()));
                return getFilmById(film.getId());
            }
            return null;
        } catch (Exception exception) {
            throw new MissingException("Фильм, который Вы пытаетесь обновить, отсутствует в базе.");
        }
    }

    @Override
    public Film deliteFilmById(int id) {
        try {
            String sql = "delete from FILM where FILM_ID = ?";
            jdbcTemplate.update(sql, id);
            return getFilmById(id);
        } catch (Exception exception) {
            throw new MissingException("Фильм, который Вы пытаетесь удалить, отсутствует в базе.");
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select f.film_id, f.NAME, f.DESCRIPTION,f.REALISE_DATE,f.DURATION,r.RATING_NAME " +
                "from Film AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs));
    }

    @Override
    public Film addLikeToFilm(int filmId, int usersId) {
        try {
            String sql = "INSERT INTO LIKES_LINE (Film_id, User_id) values " +
                    "(?,?)";
            jdbcTemplate.update(sql,
                    filmId,
                    usersId);
            return getFilmById(filmId);
        } catch (Exception exception) {
            throw new MissingException("Фильм, который Вы пытаетесь обновить, отсутствует в базе.");
        }
    }

    @Override
    public Film getFilmById(int id) {
        try {
            String sql = "select f.film_id, f.NAME, f.DESCRIPTION,f.REALISE_DATE,f.DURATION,r.RATING_NAME " +
                    "from Film AS f " +
                    "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                    "WHERE film_id = ?";
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs), id)
                    .stream()
                    .findFirst()
                    .get();
        } catch (Exception exception) {
            throw new MissingException("Фильм, который Вы пытаетесь получить, отсутствует в базе.");
        }
    }

    private Film makeFilms(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate realiseDate = rs.getDate("REALISE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        String ratingName = rs.getString("RATING_NAME");
        RatingsEnum rating = Arrays.stream(RatingsEnum.values())
                .filter(ratings -> ratings.getRating().equals(ratingName))
                .findFirst()
                .get();
        Set<Integer> likes = makeFilmsLikes(id);
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(realiseDate)
                .duration(duration)
                .genres(genresDAO.findFilmsGenres(id))
                .mpa(mpaDAO.findFilmsMPA(id))
                .likes(likes)
                .build();
    }

    public Set<Integer> makeFilmsLikes(int id) {
        Set<Integer> likes = new HashSet<>();
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM LIKES_LINE WHERE FILM_ID = ?", id);
        while (likesRows.next()) {
            likes.add(likesRows.getInt("USER_ID"));
        }
        return likes;
    }
}
