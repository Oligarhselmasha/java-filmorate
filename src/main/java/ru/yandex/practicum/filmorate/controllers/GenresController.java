package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenresController {

    private final GenresService genresService;

    @GetMapping
    public Collection<Genres> showGenres() {
        return genresService.getGenres();
    }

    @GetMapping("/{id}")
    public Genres showGenresById(@PathVariable("id") Integer id) {
        return genresService.getGenresById(id);
    }
}
