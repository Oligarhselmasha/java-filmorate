package ru.yandex.practicum.filmorate.storages;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Data
@Component
public class InMemoryUserStorage implements UserStorage {

    private int userId;
    private final Map<Integer, User> users;

    @Override
    public User addUsers(User user) {
        userId++;
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
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
    public void deliteUserById(int userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new ValidationException("Пользователь " + userId + " в базе не обнаружен");
        }
    }

    @Override
    public User getUserById(int userId) {
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

    @Override
    public User updateUsersFriend(int userId, int usersFriendId) {
        return null;
    }

    @Override
    public User deleteUsersFriend(int userId, int usersFriendId) {
        return null;
    }
}
