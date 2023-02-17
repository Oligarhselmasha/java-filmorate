package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenresDAOImpl;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class GenresService {

    private final GenresDAOImpl genresDAO;

    public Collection<Genres> getGenres() {
        return genresDAO.getGenres();
    }

    public Genres getGenresById(int id) {
        return genresDAO.getGenresById(id);
    }
}
