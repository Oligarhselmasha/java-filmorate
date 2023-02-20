package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.GenresDAOImpl;
import ru.yandex.practicum.filmorate.dao.MpaDAOImpl;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenresService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

    private final FilmService filmService;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenresService genresService;

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final MpaDAOImpl mpaDAO;
    private final GenresDAOImpl genresDAO;
    private final JdbcTemplate jdbcTemplate;
    private Film newFilm;
    private User user;

    @BeforeEach
    public void createUserAndFilmAndCleanTables() {
        newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).mpa(Mpa.builder().id(1).build())
                .build();
        user = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(1991, 03, 22))
                .build();
        jdbcTemplate.update("DELETE FROM Film CASCADE");
        jdbcTemplate.update("DELETE FROM Users CASCADE");
        jdbcTemplate.update("ALTER TABLE Users ALTER COLUMN USER_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE Film ALTER COLUMN FILM_ID RESTART WITH 1");
        userStorage.addUsers(user);
        filmDbStorage.addFilm(newFilm);
    }


    @Test
    public void testFilmOkService() {
        filmService.addFilm(newFilm);
        assertEquals(1, filmService.getFilmById(1).getId());
    }

    @Test
    void validateFilmFailNameService() {
        newFilm.setName("");
        DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            filmService.addFilm(newFilm);
        }, "Ошибка валидации");
    }

    @Test
    public void testUserOkService() {
        userService.addUsers(user);
        assertEquals(1, userService.getCustomersDyId(1).getId());
    }

    @Test
    public void testInvalidEmailService() {
        user.setEmail("kirillbulanov.ru");
        DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.addUsers(user);
        }, "Ошибка валидации");
    }

    @Test
    public void testGenreService() {
        assertEquals(genresService.getGenresById(1).getId(), 1);
    }

    @Test
    public void testMpaService() {
        assertEquals(mpaService.getMpasById(1).getName(), "G");
    }

    @Test
    public void testBadMpaService() {
        MissingException missingException = Assertions.assertThrows(MissingException.class, () -> {
            mpaService.getMpasById(8);
        }, "Ошибка валидации");
    }


    @Test
    public void testAddAndFindUserByIdStorage() {
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(userTest ->
                        assertThat(userTest).hasFieldOrPropertyWithValue("id", 1)
                )
                .hasValueSatisfying(userTest ->
                        assertThat(userTest).hasFieldOrPropertyWithValue("login", "Oligarh_s_elmasha"))
                .hasValueSatisfying(userTest ->
                        assertThat(userTest).hasFieldOrPropertyWithValue("email", "kirill-bulanov@narod.ru"))
        ;
    }

    @Test
    public void testUpdateUser() {
        User userUpdate = userStorage.getUserById(1);
        userUpdate.setEmail("kirill-bulanov@yandex.ru");
        userUpdate.setLogin("Oligarh_s_uralmasha");
        userStorage.updateUser(userUpdate);
        Optional<User> userOptional = Optional.of(userStorage.getUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(userTest ->
                        assertThat(userTest).hasFieldOrPropertyWithValue("id", 1)
                )
                .hasValueSatisfying(userTest ->
                        assertThat(userTest)
                                .hasFieldOrPropertyWithValue("login", "Oligarh_s_uralmasha"))
                .hasValueSatisfying(userTest ->
                        assertThat(userTest)
                                .hasFieldOrPropertyWithValue("email", "kirill-bulanov@yandex.ru"));
    }

    @Test
    public void testAddAndFindFilmByIdStorage() {
        Optional<Film> filmOptional = Optional.of(filmDbStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("id", 1)
                )
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("name", "Крестный Отец"))
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("description", "Фильм про мафию"))
        ;
    }

    @Test
    public void testUpdateFilm() {
        Film filmUpdate = filmDbStorage.getFilmById(1);
        filmUpdate.setName("Крестный отчим");
        filmUpdate.setDescription("Фильм про отчима");
        filmDbStorage.updateFilm(filmUpdate);
        Optional<Film> filmOptional = Optional.of(filmDbStorage.getFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("id", 1)
                )
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("name", "Крестный отчим"))
                .hasValueSatisfying(filmTest ->
                        assertThat(filmTest).hasFieldOrPropertyWithValue("description", "Фильм про отчима"))
        ;
    }

    @Test
    public void deleteFilm() {
        filmDbStorage.deliteFilmById(1);
        MissingException missingException = Assertions.assertThrows(MissingException.class, () -> {
            filmDbStorage.getFilmById(1);
        }, "Ошибка валидации");
    }

    @Test
    public void deleteUser() {
        userStorage.deliteUserById(1);
        MissingException missingException = Assertions.assertThrows(MissingException.class, () -> {
            userStorage.getUserById(1);
        }, "Ошибка валидации");
    }

    @Test
    public void testMpa() {
        Optional<Mpa> mpaOptional = Optional.of(mpaDAO.getMpaById(1));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpaTest ->
                        assertThat(mpaTest).hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    public void testGenres() {
        Optional<Genres> genresOptional = Optional.of(genresDAO.getGenresById(1));
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genreTest ->
                        assertThat(genreTest).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

}