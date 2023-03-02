package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping("/users")
    public ArrayList<User> homePage() {
        log.debug("Получен запрос GET /users.");
        return userStorage.getAllUsers();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        return userStorage.createUser(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        return userStorage.updateUser(user);
    }
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}");
        userService.addFriend(userId,friendId);
    }
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        log.debug("Получен запрос DELETE /users/{id}/friends/{friendId}");
        userService.deleteFriend(userId,friendId);
    }
    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriend(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос GET /users/{id}/friends");
       return userService.getAllFriend(userId);
    }
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        log.debug("Получен запрос GET /users/{id}.");
        return userStorage.getUserById(userId);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer otherId) {
        log.debug("Получен запрос GET /users/{id}/friends/common/{otherId}");
        return userService.getCommonFriend(userId,otherId);
    }
}
