package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    @Autowired
    UserStorage userStorage;

    private int id;

    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likes = new HashSet<>();

    public void setLikes(int userId) {
        likes.add(userId);
    }

    public void deleteLikes(int userId) throws MissingException {
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            throw new MissingException(String.format("Пользователь с id %s не ставил фильму лайк", userId));
        }
    }
}
