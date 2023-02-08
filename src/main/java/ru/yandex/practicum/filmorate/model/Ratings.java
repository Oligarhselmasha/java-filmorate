package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Ratings {
    G ("G"),
    PG ("PG"),
    PG13 ("PG-13"),
    R ("R"),
    NC17 ("NC-17");

    private final String rating;

    public String getRating() {
        return rating;
    }
}
