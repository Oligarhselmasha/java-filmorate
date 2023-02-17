package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenresDAO {
    List<Genres> findFilmsGenres(int id);

    List<Genres> getGenres();

    Genres getGenresById(int id);
}
