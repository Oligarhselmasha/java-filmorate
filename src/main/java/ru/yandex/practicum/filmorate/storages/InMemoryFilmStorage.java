package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private int filmId;
    private final Map<Integer, Film> films;

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Данные в запросе на добавление нового фильма не соответствуют критериям.");
        }
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new MissingException("Фильм, который Вы пытаетесь обновить, отсутствует в базе.");
        }
    }

    @Override
    public Film deliteFilmById(int id) {
        if (films.containsKey(id)) {
            return films.remove(id);
        } else {
            throw new ValidationException("Фильм, который Вы пытаетесь удалить, отсутствует в базе.");
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) throws MissingException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new MissingException(String.format("Фильм с id %s отсуствует в базе", id));
        }
    }
}
