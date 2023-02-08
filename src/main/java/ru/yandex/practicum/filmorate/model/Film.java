package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.MissingException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private List<Genres> genres;
    private Ratings rating;
    private final Set<Integer> likes = new HashSet<>();

    public void setLike(int userId) {
        likes.add(userId);
    }

    public Integer getCountLikes(){
        return likes.size();
    }

    public void deleteLikes(int userId) {
        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            throw new MissingException(String.format("Пользователь с id %s не ставил фильму лайк", userId));
        }
    }
}
