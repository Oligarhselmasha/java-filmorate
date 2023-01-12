package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    int userId;

    private final List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }


    @PostMapping()
    public User addUser(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin().isEmpty() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Данные в запросе на добавление нового пользователя не соответствуют " +
                    "критериям.");
            throw new ValidationException("Данные в запросе на добавление нового пользователя не соответствуют " +
                    "критериям.");
        }
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
        userId++;
        user.setId(userId);
        users.add(user);
            log.debug("Пользователь {} успешно добавлен!", user.getLogin());
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.remove(user)) {
            users.add(user);
            return user;

        } else {
            log.warn("Пользователь {}, которого Вы пытаетесь обновить, в базе не обнаружен", user.toString());
            throw new ValidationException("Пользователь в базе не обнаружен");
        }
    }
}
