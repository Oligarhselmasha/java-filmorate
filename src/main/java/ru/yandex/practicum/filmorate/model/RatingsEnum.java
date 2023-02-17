package ru.yandex.practicum.filmorate.model;

public enum RatingsEnum {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private final String rating;

    RatingsEnum(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }
}
