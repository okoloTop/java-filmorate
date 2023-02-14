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

    public User createUser(User user) {
        if (isValid(user)) {
            user.setId(++assignmentId);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Пользователь " + user.getName() + " не будет добавлен,проверьте правильность введенных данных");
        }
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
        if (isValid(user)) {
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ValidationException("Пользователь " + user.getName() + " не будет добавлен,проверьте правильность введенных данных");
        }
    }

    public boolean isValid(User user) {
        Boolean valid = true;
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            return valid = false;
        }
        if (StringUtils.containsWhitespace(user.getLogin()) || user.getLogin().isBlank() || user.getLogin() == null) {
            return valid = false;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            return valid = false;
        }
        return valid;
    }

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
}
