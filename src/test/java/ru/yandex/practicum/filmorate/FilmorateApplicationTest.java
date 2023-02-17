package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenresService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

//!!!Тесты запускать все разом. Порядок запуска тестов имеет значение!!!

class FilmoRateApplicationTests {

    private final FilmService filmService;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenresService genresService;

    @Test
    public void testFilmOk() {
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).mpa(Mpa.builder().id(1).build())
                .build();
        filmService.addFilm(newFilm);
        assertEquals(2, filmService.getFilmById(2).getId());
    }

    @Test
    void validateFilmFailName() {
        Film newFilm = Film.builder()
                .name("")
                .description("Фильм")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).mpa(Mpa.builder().id(1).build())
                .build();

        DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            filmService.addFilm(newFilm);
        }, "Ошибка валидации");
    }
    @Test
    public void testUserOk() {
        User user = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(1991, 03,22))
                .build();
        userService.addUsers(user);
        assertEquals(2, userService.getCustomersDyId(2).getId());
    }
    @Test
    public void testInvalidEmail() {
        User user = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill-bulanovnarod.ru")
                .birthday(LocalDate.of(1991, 03,22))
                .build();
        DataIntegrityViolationException dataIntegrityViolationException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userService.addUsers(user);
        }, "Ошибка валидации");
    }

    @Test
    public void testGenre() {
       assertEquals(genresService.getGenresById(1).getId(), 1);
    }

    @Test
    public void testMpa() {
        assertEquals(mpaService.getMpasById(1).getName(), "G");
    }

    @Test
    public void testBadMpa() {
        MissingException missingException = Assertions.assertThrows(MissingException.class, () -> {
            mpaService.getMpasById(8);
        }, "Ошибка валидации");
    }
}