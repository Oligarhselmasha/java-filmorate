package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;

    @BeforeEach
    public void createUserAndFilmAndCleanTables() {
        User user = User.builder()
                .login("Oligarh_s_elmasha")
                .name("Kirill")
                .email("kirill-bulanov@narod.ru")
                .birthday(LocalDate.of(1991, 03, 22))
                .build();
        jdbcTemplate.update("DELETE FROM Users CASCADE");
        jdbcTemplate.update("ALTER TABLE Users ALTER COLUMN USER_ID RESTART WITH 1");
        userStorage.addUsers(user);
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
}