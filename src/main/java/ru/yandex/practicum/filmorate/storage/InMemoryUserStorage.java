package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    protected int assignmentId = 0;
    protected HashMap<Integer, User> users = new HashMap<>();


    @Override
    public User createUser(User user) {
        user.setId(++assignmentId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        user.setLikes(new HashSet<>());
        user.setFriends(new HashSet<>());
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getName(), user.getId(), users.size());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(int userId) {
        if (!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);

    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NullPointerException("Пользователь c id" + user.getId() + " не найден");
        }
        if (user.getLikes() == null) {
            user.setLikes(new HashSet<>());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getName(), user.getId(), users.size());
        return users.get(user.getId());
    }

    @Override
    public User getUserById(Integer id) {
        if (users.get(id) == null) {
            throw new NullPointerException();
        }
        log.debug("Получен пользователь c  ID: {}; ",id);
        return users.get(id);
    }

    @Override
    public List<User> getCommonFriend(Integer userId, Integer friendId) {
        List<User> commonFriend = new ArrayList<>();
        final User user = getUserById(userId);
        final User friend = getUserById(friendId);
        for (Integer s : user.getFriends()) {
            if (friend.getFriends().contains(s)) {
                commonFriend.add(getUserById(s));
            }
        }
        log.debug("Получен список общих друзей пользователя c  ID: {}; и пользователя с ID: {}", userId, friendId);
        return commonFriend;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        if (getUserById(userId) == null || getUserById(friendId) == null) {
            throw new NullPointerException("Пользователя нет в базе, проверьте id пользователя");
        }
        final User user = getUserById(userId);
        final User friend = getUserById(friendId);
        if (!user.getFriends().contains(friendId)) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            log.debug("Пользователь c  ID: {}; добавил в друзья пользователя с ID: {}", userId, friendId);
        } else {
            throw new ValidationException("Вы уже добавили в друзья этого пользователя с id " + friendId);
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (getUserById(userId).getFriends().contains(friendId)) {
            final User user = getUserById(userId);
            final User friend = getUserById(friendId);
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            log.debug("Пользователь c  ID: {}; удалил из друзей пользователя с ID: {}", userId, friendId);
        } else {
            throw new ValidationException("У вас нет пользователя с id " + friendId + " в друзьях");
        }
    }

    @Override
    public List<User> getAllFriend(Integer userId) {
        List<User> friend = new ArrayList<>();
        User user = getUserById(userId);
        for (Integer s : user.getFriends()) {
            friend.add(getUserById(s));
        }
        log.debug("Получен список всех друзей пользователя c  ID: {};", userId);
        return friend;
    }
}
