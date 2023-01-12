package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    int duration;
}
