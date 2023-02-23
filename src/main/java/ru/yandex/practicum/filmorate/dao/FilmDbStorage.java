package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.ExceptionMessageEnum.BAD_FILM;
import static ru.yandex.practicum.filmorate.model.SqlRequestsEnum.DELETE_GENRE_FROM_FILM;

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
            throw new ValidationException(BAD_FILM.getException());
        }
        String sql = "INSERT INTO film (title, " +
                "        description, " +
                "        realise_date, " +
                "        duration, " +
                "        rating_id) values " +
                "(?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setString(5, film.getMpa().toString());
            return ps;
        }, keyHolder);
        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        if (film.getGenres() != null) {
            String sqlGenreAdd = "INSERT INTO genres_line (film_id, genre_id) " +
                    "values " +
                    "(?, ?)";
            film.getGenres()
                    .forEach(genre -> jdbcTemplate
                            .update(sqlGenreAdd, filmId, genre.toString()));
        }
        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            String sql = "UPDATE film SET title = ?, description = ?, realise_date = ?, duration = ?, rating_id = ? WHERE film_id = ?; ";
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
            if (film.getGenres().isEmpty()) {
                String sqlDeleteGenres = DELETE_GENRE_FROM_FILM.getRequest();
                jdbcTemplate.update(sqlDeleteGenres, film.getId());
                return getFilmById(film.getId());
            }
            String sqlDeleteGenres = DELETE_GENRE_FROM_FILM.getRequest();
            jdbcTemplate.update(sqlDeleteGenres, film.getId());
            String sqlGenreAdd = "INSERT INTO genres_line (film_id, genre_id) " +
                    "values " +
                    "(?, ?)";
            film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList())
                    .forEach(g -> jdbcTemplate.update(sqlGenreAdd, film.getId(), g.toString()));
            return getFilmById(film.getId());
        } catch (Exception exception) {
            throw new MissingException(BAD_FILM.getException());
        }
    }

    @Override
    public void deliteFilmById(int id) {
        try {
            String sql = "delete from film where film_id = ?";
            jdbcTemplate.update(sql, id);
        } catch (Exception exception) {
            throw new MissingException(BAD_FILM.getException());
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select f.film_id, f.title, f.description ,f.realise_date ,f.duration, r.rating_name " +
                "from film AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs));
    }

    @Override
    public Film addLikeToFilm(int filmId, int usersId) {
        try {
            String sql = "INSERT INTO likes_line (film_id, user_id) values " +
                    "(?,?)";
            jdbcTemplate.update(sql,
                    filmId,
                    usersId);
            return getFilmById(filmId);
        } catch (Exception exception) {
            throw new MissingException(BAD_FILM.getException());
        }
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "select f.film_id, f.title, f.description, f.realise_date, f.duration ,r.rating_name " +
                "from film AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs), id)
                .stream()
                .findFirst().orElseThrow(() -> new MissingException(BAD_FILM.getException()));
    }

    private Film makeFilms(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("title");
        String description = rs.getString("description");
        LocalDate realiseDate = rs.getDate("realise_date").toLocalDate();
        int duration = rs.getInt("duration");
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
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM likes_line WHERE film_id = ?", id);
        while (likesRows.next()) {
            likes.add(likesRows.getInt("user_id"));
        }
        return likes;
    }
}
