package ru.yandex.practicum.filmorate.exceptions;

public class MissingException extends RuntimeException {
    public MissingException(String message) {
        super(message);
    }
}
