package ru.yandex.practicum.filmorate.model;

public enum ExceptionMessageEnum {
    BAD_FILM("Неправильный запрос фильма"),
    BAD_USER("Неправильный запрос пользователя"),
    BAD_GENRE("Неправильный запрос жанра"),
    BAD_MPA("Неправильный запрос рейтинга");

    private final String exception;

    ExceptionMessageEnum(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }
}
