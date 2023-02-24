package ru.yandex.practicum.filmorate.model;

public enum SqlRequestsEnum {
    DELETE_GENRE_FROM_FILM("delete from Genres_line where FILM_ID = ?");

    private final String request;

    SqlRequestsEnum(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
