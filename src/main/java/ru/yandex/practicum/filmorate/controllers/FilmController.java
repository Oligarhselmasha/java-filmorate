package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();

    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) throws MissingException { // Получение фильма по id
        return filmService.getFilmById(id);

    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws MissingException {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) throws MissingException {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLikeFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) throws MissingException {
        return filmService.removeLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> receiveMostPopular(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.receiveMostPopular(count);
    }

    public static void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Имя не соответствует заданным критериям");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не соответствует заданным критериям");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность не соответствует заданным критериям");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не соответствует заданным критериям");
        }
    }
}
