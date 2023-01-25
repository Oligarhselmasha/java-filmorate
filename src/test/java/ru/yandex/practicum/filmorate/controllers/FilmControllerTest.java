//package ru.yandex.practicum.filmorate.controllers;
//
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class FilmControllerTest {
//
//    FilmController filmController = new FilmController();
//
//    @Test
//    void validateFilmOk() throws ValidationException {
//        final Film film = new Film();
//        film.setName("Терминатор");
//        film.setDescription("Фильм о роботах");
//        film.setDuration(120);
//        film.setReleaseDate(LocalDate.of(1985, 10, 10));
//        FilmController.validate(film);
//    }
//
//    @Test
//    void validateFilmFailName() {
//        final Film film = new Film();
//        film.setName("");
//        film.setDescription("Фильм о роботах");
//        film.setDuration(120);
//        film.setReleaseDate(LocalDate.of(1985, 10, 10));
//        ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
//        assertEquals(exception.getMessage(), "Имя не соответствует заданным критериям");
//    }
//
//    @Test
//    void validateFilmFailDescription() {
//        final Film film = new Film();
//        film.setName("Терминатор");
//        film.setDescription("Фильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботах" +
//                "Фильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботах" +
//                "Фильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботах" +
//                "Фильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботахФильм о роботах");
//        film.setDuration(120);
//        film.setReleaseDate(LocalDate.of(1985, 10, 10));
//        ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
//        assertEquals(exception.getMessage(), "Описание не соответствует заданным критериям");
//    }
//
//    @Test
//    void validateFilmFailDuration() {
//        final Film film = new Film();
//        film.setName("Терминатор");
//        film.setDescription("Фильм о роботах");
//        film.setDuration(-1);
//        film.setReleaseDate(LocalDate.of(1985, 10, 10));
//        ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
//        assertEquals(exception.getMessage(), "Продолжительность не соответствует заданным критериям");
//    }
//
//    @Test
//    void validateFilmFailReleaseDate() {
//        final Film film = new Film();
//        film.setName("Терминатор");
//        film.setDescription("Фильм о роботах");
//        film.setDuration(120);
//        film.setReleaseDate(LocalDate.of(985, 10, 10));
//        ValidationException exception = assertThrows(ValidationException.class, () -> FilmController.validate(film));
//        assertEquals(exception.getMessage(), "Дата релиза не соответствует заданным критериям");
//    }
//}