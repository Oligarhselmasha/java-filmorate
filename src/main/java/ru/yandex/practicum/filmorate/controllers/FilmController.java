package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    int filmId;

    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }


    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (film.getDuration()<=0 || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) ||
                film.getName().isEmpty()) {
            log.warn("Данные в запросе на добавление нового фильма не соответствуют критериям.");
            throw new ValidationException("Данные в запросе на добавление нового фильма не соответствуют критериям.");

        }
        filmId++;
        film.setId(filmId);
        films.add(film);
        log.debug("Добавлен фильм: {}", film.toString());
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.remove(film)) {
            films.add(film);
            log.debug("Обновлен фильм фильм: {}", film.toString());
            return film;
        } else {
            log.warn("Фильм {} отсутствует в базе, обновлять нечего", film.getName());
            throw new ValidationException("Фильм, который Вы пытаетесь обновить, отсутствует в базе.");
        }

    }
}
