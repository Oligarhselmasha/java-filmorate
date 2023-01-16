package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int userId;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getCustomers() {
        return users.values();
    }

    @PostMapping()
    public User addUsers(@Valid @RequestBody User user) throws ValidationException {
        if (user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Данные в запросе на добавление нового пользователя не соответствуют " +
                    "критериям.");
            throw new ValidationException("Данные в запросе на добавление нового пользователя не соответствуют " +
                    "критериям.");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        userId++;
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь {} успешно добавлен!", user.getLogin());
        return user;
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь {} успешно обновлен!", user.getLogin());
            return user;
        } else {
            log.warn("Пользователь {}, которого Вы пытаетесь обновить, в базе не обнаружен", user);
            throw new ValidationException("Пользователь в базе не обнаружен");
        }
    }

    public static void validate(User user) throws ValidationException {
        if(!user.getEmail().contains("@")){
            throw new ValidationException("Email не соответствует заданным критериям");
        }
        if(user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")){
            throw new ValidationException("Логин не соответствует заданным критериям");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения не должна быть в будущем");
        }
    }
}
