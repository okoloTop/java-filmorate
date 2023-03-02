package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        valid(user);
        user.setId(++assignmentId);
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
        valid(user);
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

    private void valid(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (StringUtils.containsWhitespace(user.getLogin()) || user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
