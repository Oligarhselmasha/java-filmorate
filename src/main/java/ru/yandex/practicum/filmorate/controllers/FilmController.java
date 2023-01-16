package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int filmId;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Данные в запросе на добавление нового фильма не соответствуют критериям.");
            throw new ValidationException("Данные в запросе на добавление нового фильма не соответствуют критериям.");
        }
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм фильм: {}", film);
            return film;
        } else {
            log.warn("Фильм {} отсутствует в базе, обновлять нечего", film.getName());
            throw new ValidationException("Фильм, который Вы пытаетесь обновить, отсутствует в базе.");
        }
    }

    public static void validate(Film film) throws ValidationException {
        if(film.getName() == null || film.getName().isEmpty()){
            throw new ValidationException("Имя не соответствует заданным критериям");
        }
        if(film.getDescription().length() > 200){
            throw new ValidationException("Описание не соответствует заданным критериям");
        }
        if(film.getDuration() <= 0){
            throw new ValidationException("Продолжительность не соответствует заданным критериям");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new ValidationException("Дата релиза не соответствует заданным критериям");
        }
    }
}
