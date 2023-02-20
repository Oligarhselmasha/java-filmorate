package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deliteFilmById(int id);

    List<Film> getFilms();

    Film getFilmById(int id);

    Film addLikeToFilm(int userId, int usersFriendId);
}
