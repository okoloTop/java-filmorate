package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void initFilmController() {
        userController = new UserController();
    }

    @Test
    void createValidUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userController.createUser(user);
        assertEquals(1,userController.getAllUsers().size());
    }
    @Test
    void updateUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userController.createUser(user);
        User user2 = User.builder()
                .id(1)
                .name("Тест2")
                .login("Тестлогин2")
                .email("current@email.ru")
                .birthday(LocalDate.of(1974, 12, 3))
                .build();
        user = user2;
        userController.updateUser(user);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(user.getLogin(),user2.getLogin());
    }
    @Test
    void createBlancEmailUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        });
        assertNotNull(thrown.getMessage());
    }
    @Test
    void createEmailMistakeUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("nnmm.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        });
        assertNotNull(thrown.getMessage());
    }
    @Test
    void createBlancUserName() {
        user = User.builder()
                .name("")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userController.createUser(user);
        assertEquals(user.getName(),user.getLogin());
    }
    @Test
    void createUserBirthdayMistake() {
        user = User.builder()
                .name("")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(2023, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            userController.createUser(user);
        });
        assertNotNull(thrown.getMessage());
    }
}


