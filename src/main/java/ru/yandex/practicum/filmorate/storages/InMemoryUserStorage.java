package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int userId;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUsers(User user) throws ValidationException {
        if (user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Данные в запросе на добавление нового пользователя не соответствуют " +
                    "критериям.");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        userId++;
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(User user) throws MissingException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            return user;
        } else {
            throw new MissingException("Пользователь " + user.getId() + " в базе не обнаружен");
        }
    }

    @Override
    public User deliteUserById(int userId) throws ValidationException {
        if (users.containsKey(userId)) {
            return users.remove(userId);
        } else {
            throw new ValidationException("Пользователь " + userId + " в базе не обнаружен");
        }
    }

    @Override
    public User getUserById(int userId) throws MissingException {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new MissingException("Пользователь " + userId + " в базе не обнаружен");
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public Collection<Integer> getUsersIds() {
        return users.keySet();
    }
}
