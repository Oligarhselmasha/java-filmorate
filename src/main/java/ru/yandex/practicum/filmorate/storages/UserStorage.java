package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public interface UserStorage {
    int userId = 0;
    Map<Integer, User> users = new HashMap<>();

    User addUsers(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException, MissingException;
    User deliteUserById(int userId) throws ValidationException;
    User getUserById(int userId) throws MissingException;
    Collection<User> getUsers();
    Collection<Integer> getUsersIds();

}
