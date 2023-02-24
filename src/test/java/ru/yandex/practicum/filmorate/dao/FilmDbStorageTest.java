package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void createNewFilmAndCleanTables() {
        jdbcTemplate.update("DELETE FROM film CASCADE");
        jdbcTemplate.update("ALTER TABLE film ALTER COLUMN film_id RESTART WITH 1");
        Film newFilm = Film.builder()
                .name("Крестный Отец")
                .description("Фильм про мафию")
                .duration(180)
                .releaseDate(LocalDate.of(1985, 11, 11)).mpa(Mpa.builder().id(1).build())
                .build();
        filmDbStorage.addFilm(newFilm);
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
}