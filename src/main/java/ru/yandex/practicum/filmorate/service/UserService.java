package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MissingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public User getCustomersDyId(Integer id) { // Получение юзера по id
        return userStorage.getUserById(id);
    }

    public Collection<User> getCustomers() { // Получение всех юзеров
        return userStorage.getUsers();
    }

    public User addUsers(User user) {
        return userStorage.addUsers(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User deliteUserById(Integer id) {
        return userStorage.deliteUserById(id);
    }

    public User addToFriends(int userId, int friendId) {
        if (!userStorage.getUsersIds().contains(userId) || !userStorage.getUsersIds().contains(friendId)) {
            throw new MissingException("Введен несуществующий id");
        }
        User user = userStorage.getUserById(userId);
        user.setNotAprovedFriendsById(friendId);
        return user;
    }

    public User aproveFriendship(int userId, int friendId) {
        if (!userStorage.getUsersIds().contains(userId) || !userStorage.getUsersIds().contains(friendId)) {
            throw new MissingException("Введен несуществующий id");
        }
        User user1 = userStorage.getUserById(userId);
        User user2 = userStorage.getUserById(friendId);
        user2.setAprovedFriendsById(userId);
        user1.setAprovedFriendsById(friendId);
        return user1;
    }

    public User deliteFromFriends(int userId, int friendId) {
        userStorage.getUserById(userId).deleteFriends(friendId);
        return userStorage.getUserById(friendId);
    }

    public Collection<User> takeFriendsList(int userId) {
        Collection<User> friends = new ArrayList<>();
        if (userStorage.getUserById(userId).getFriends().isEmpty()) {
            return friends;
        }
        Set<Integer> frendsIds = userStorage.getUserById(userId).getFriends();
        frendsIds.forEach(id -> friends.add(userStorage.getUserById(id)));
        return friends;
    }

    public Collection<User> takeCommonFriendsList(int userId, int otherUserId) {
        Collection<User> commonFriends = new ArrayList<>();
            if (userStorage.getUserById(userId).getFriends().isEmpty() || userStorage.getUserById(userId).getFriends() == null) {
                return commonFriends;
            }
        Set<Integer> frendsIds = userStorage.getUserById(userId).getFriends();
        Set<Integer> otherFrendsIds = userStorage.getUserById(otherUserId).getFriends();
        frendsIds.stream()
                .filter(otherFrendsIds::contains)
                .collect(Collectors.toList())
                .forEach(id -> commonFriends.add(userStorage.getUserById(id)));
            return commonFriends;
    }
}
