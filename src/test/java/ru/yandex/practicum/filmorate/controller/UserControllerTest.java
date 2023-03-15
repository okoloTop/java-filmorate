package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    UserService userService;
    private final UserStorage userStorage;
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    User user;

    @BeforeEach
    void initFilmController() {
        userService = new UserService(userStorage);
    }

    @Test
    void createValidUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userService.createUser(user);
        assertEquals(user.getName(), userService.getUserById(user.getId()).getName());
    }

    @Test
    void updateUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userService.createUser(user);
        User user2 = User.builder()
                .id(1)
                .name("Тест2")
                .login("Тестлогин2")
                .email("current@email.ru")
                .birthday(LocalDate.of(1974, 12, 3))
                .build();
        user = user2;
        userService.updateUser(user);
        assertEquals(user.getLogin(), user2.getLogin());
    }

    @Test
    void createBlancEmailUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        Set<ConstraintViolation<User>> violations = this.validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createEmailMistakeUser() {
        user = User.builder()
                .name("Тест")
                .login("Тестлогин")
                .email("nnmm.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        Set<ConstraintViolation<User>> violations = this.validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createBlancUserName() {
        user = User.builder()
                .name("")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(1972, 12, 3))
                .build();
        userService.createUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void createUserBirthdayMistake() {
        user = User.builder()
                .name("")
                .login("Тестлогин")
                .email("current@email.ru")
                .birthday(LocalDate.of(2023, 12, 3))
                .build();
        Set<ConstraintViolation<User>> violations = this.validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}


