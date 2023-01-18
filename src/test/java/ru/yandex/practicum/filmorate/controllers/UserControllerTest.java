package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTest {

    UserController userController = new UserController();

    @Test
    void validateFilmOk() throws ValidationException {
        final User user = new User();
        user.setLogin("Кирилл");
        user.setEmail("kirill@kirill.ru");
        user.setBirthday(LocalDate.of(1991, 3, 22));
        UserController.validate(user);
    }

    @Test
    void validateUserFailLogin() {
        final User user = new User();
        user.setLogin("");
        user.setEmail("kirill@kirill.ru");
        user.setBirthday(LocalDate.of(1991, 3, 22));
        ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals(exception.getMessage(), "Логин не соответствует заданным критериям");
    }

    @Test
    void validateFilmFailEmail() {
        final User user = new User();
        user.setLogin("Кирилл");
        user.setEmail("kirillkirill.ru");
        user.setBirthday(LocalDate.of(1991, 3, 22));
        ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals(exception.getMessage(), "Email не соответствует заданным критериям");
    }

    @Test
    void validateFilmFailDOB() {
        final User user = new User();
        user.setLogin("Кирилл");
        user.setEmail("kirill@kirill.ru");
        user.setBirthday(LocalDate.of(2991, 3, 22));
        ValidationException exception = assertThrows(ValidationException.class, () -> UserController.validate(user));
        assertEquals(exception.getMessage(), "Дата рождения не должна быть в будущем");
    }
}