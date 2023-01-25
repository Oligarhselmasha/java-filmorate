package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(Integer id) throws MissingException { // Получение фильма по id
        return filmStorage.getFilmById(id);
    }

    @PostMapping()
    public Film addFilm(Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@Valid @RequestBody Film film) throws MissingException {
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(int id, int userId) throws MissingException {
        filmStorage.getFilmById(id).setLikes(userId);
        return filmStorage.getFilmById(id);
    }

    public Film removeLikeFilm(int id, int userId) throws MissingException {
        filmStorage.getFilmById(id).deleteLikes(userId);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> receiveMostPopular(int count) {
        return filmStorage.getFilms().stream().sorted(this::compare).limit(count).collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikes().size() - f0.getLikes().size();
    }
}
