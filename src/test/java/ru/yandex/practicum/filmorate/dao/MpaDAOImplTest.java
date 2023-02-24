package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDAOImplTest {
    private final MpaDAOImpl mpaDAO;

    @Test
    public void testMpa() {
        Optional<Mpa> mpaOptional = Optional.of(mpaDAO.getMpaById(1));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpaTest ->
                        assertThat(mpaTest).hasFieldOrPropertyWithValue("name", "G")
                );
    }

}