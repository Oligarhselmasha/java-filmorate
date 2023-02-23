package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenresDAOImplTest {
    private final GenresDAOImpl genresDAO;


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