package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {
    protected int assignmentId = 0;
    protected HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public ArrayList<User> homePage() {
        log.debug("Получен запрос GET /users.");
        return getAllUsers();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        return createUser(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        return updateUser(user);
    }

    public User createUser(User user) {
        isValid(user);
        user.setId(++assignmentId);
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getName(), user.getId(), users.size());
        return user;
    }

    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(int userId) {
        if (!users.containsKey(userId)) {
            return;
        }
        users.remove(userId);

    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь c id" + user.getId() + " не найден");
        }
        isValid(user);
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь: {}; его ID: {}; всего пользователей в базе: {}", user.getName(), user.getId(), users.size());
        return users.get(user.getId());
    }

    public void isValid(User user) {
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
