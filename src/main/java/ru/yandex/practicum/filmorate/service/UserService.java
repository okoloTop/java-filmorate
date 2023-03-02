package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new NullPointerException("Пользователя нет в базе, проверьте id пользователя");
        }
        if (!userStorage.getUserById(userId).getFriends().contains(friendId)) {
            userStorage.getUserById(userId).getFriends().add(friendId);
            userStorage.getUserById(friendId).getFriends().add(userId);
            log.debug("Пользователь c  ID: {}; добавил в друзья пользователя с ID: {}",userId,friendId);
        } else {
            throw new ValidationException("Вы уже добавили в друзья этого пользователя с id " + friendId);
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (userStorage.getUserById(userId).getFriends().contains(friendId)) {
            userStorage.getUserById(userId).getFriends().remove(friendId);
            userStorage.getUserById(friendId).getFriends().remove(userId);
            log.debug("Пользователь c  ID: {}; удалил из друзей пользователя с ID: {}",userId,friendId);
        } else {
            throw new ValidationException("У вас нет пользователя с id " + friendId + " в друзьях");
        }
    }

    public List<User> getAllFriend(Integer userId) {
        List<User> friend = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        for (Integer s : user.getFriends()) {
            friend.add(userStorage.getUserById(s));
        }
        log.debug("Получен список всех друзей пользователя c  ID: {};",userId);
        return friend;
    }

    public List<User> getCommonFriend(Integer userId, Integer friendId) {
        List<User> commonFriend = new ArrayList<>();
        User user = userStorage.getUserById(userId);
        for (Integer s : user.getFriends()) {
            if (userStorage.getUserById(friendId).getFriends().contains(s)) {
                commonFriend.add(userStorage.getUserById(s));
            }
        }
        log.debug("Получен список общих друзей пользователя c  ID: {}; и пользователя с ID: {}",userId,friendId);
        return commonFriend;
    }
}
