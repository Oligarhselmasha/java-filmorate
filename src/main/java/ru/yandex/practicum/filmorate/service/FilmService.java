package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(Integer id) { // Получение фильма по id
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(int id, int userId) {
        filmStorage.addLikeToFilm(id, userId);
        return filmStorage.getFilmById(id);
    }

    public Film removeLikeFilm(int id, int userId) {
        filmStorage.getFilmById(id).deleteLikes(userId);
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> receiveMostPopular(int count) {
        List<Film> list = filmStorage.getFilms();
        list.sort(Comparator.comparingInt(Film::getCountLikes).reversed());
        return list.stream()
                .limit(count)
                .collect(Collectors.toList());
    }
}
