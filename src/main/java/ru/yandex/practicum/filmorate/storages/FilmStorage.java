package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public interface FilmStorage {
    int filmId = 0;
    Map<Integer, Film> films = new HashMap<>();

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws MissingException;

    Film deliteFilmById(int id) throws ValidationException;

    Collection<Film> getFilms();

    Film getFilmById(int id) throws MissingException;
}
