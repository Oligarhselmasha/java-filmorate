package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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


}
