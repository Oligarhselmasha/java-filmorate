package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GenresEnum {
    Comedy("Комедия"),
    Drama("Драма"),
    Mult("Мультфильм"),
    Triller("Триллер"),
    Doc("Документальный"),
    Action("Боевик");

    private final String genre;

    public String getRating() {
        return genre;
    }
}
