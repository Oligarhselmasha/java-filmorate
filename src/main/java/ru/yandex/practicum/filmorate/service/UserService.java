package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getCustomersDyId(Integer id) throws MissingException { // Получение юзера по id
        return userStorage.getUserById(id);
    }

    public Collection<User> getCustomers() { // Получение всех юзеров
        return userStorage.getUsers();
    }

    public User addUsers(User user) throws ValidationException {
        return userStorage.addUsers(user);
    }

    public User updateUser(User user) throws MissingException {
        return userStorage.updateUser(user);
    }

    public User deliteUserById(Integer id) throws ValidationException {
        return userStorage.deliteUserById(id);
    }

    public User addToFriends(int userId, int friendId) throws MissingException {
        if (!userStorage.getUsersIds().contains(userId) || !userStorage.getUsersIds().contains(friendId)) {
            throw new MissingException("Введен несуществующий id");
        }
        User user1 = userStorage.getUserById(userId);
        user1.setFriends(friendId);
        User user2 = userStorage.getUserById(friendId);
        user2.setFriends(userId);
        return user1;
    }

    public User deliteFromFriends(int userId, int friendId) throws MissingException {
        userStorage.getUserById(userId).deleteFriends(friendId);
        return userStorage.getUserById(friendId);
    }

    public Collection<User> takeFriendsList(int userId) throws MissingException {
        Collection<User> friends = new ArrayList<>();
        if (userStorage.getUserById(userId).getFriends().isEmpty()) {
            return friends;
        }
        for (int id : userStorage.getUserById(userId).getFriends()) {
            friends.add(userStorage.getUserById(id));
        }
        return friends;
    }

    public Collection<User> takeCommonFriendsList(int userId, int otherUserId) throws MissingException {
        Collection<User> commonFriends = new ArrayList<>();
            if (userStorage.getUserById(userId).getFriends().isEmpty()) {
                return commonFriends;
            }
            for (int id : userStorage.getUserById(userId).getFriends()) {
                if (userStorage.getUserById(otherUserId).getFriends().contains(id)) {
                    commonFriends.add(userStorage.getUserById(id));
                }
            }
            return commonFriends;
    }
}
