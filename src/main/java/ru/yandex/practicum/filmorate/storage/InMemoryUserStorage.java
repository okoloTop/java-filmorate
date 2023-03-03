package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected int assignmentId = 0;
    protected HashMap<Integer, User> users = new HashMap<>();


    @Override
    public User createUser(User user) {
        user.setId(++assignmentId);
        if(user.getName() == null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        user.setLikes(new HashSet<>());
        user.setFriends(new HashSet<>());
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getName(), user.getId(), users.size());
        return user;
    }

    @Override
    public ArrayList<User> getAllUsers() {
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
}
