package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component
public interface UserStorage {
    User addUsers(User user);
    User updateUser(User user);
    User deliteUserById(int userId);
    User getUserById(int userId);
    Collection<User> getUsers();
    Collection<Integer> getUsersIds();
}
