package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data

public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();


    public Set<Integer> getFriends() {
        return friends;
    }

    public void deleteFriends(int userId) {
        friends.remove(userId);
    }

    public void setFriends(Integer userId) {
        this.friends.add(userId);
    }
}
