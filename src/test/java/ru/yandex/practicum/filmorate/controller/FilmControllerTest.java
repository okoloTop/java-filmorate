package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class FilmControllerTest {
    FilmService filmService;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Film film;

    public FilmControllerTest(@Qualifier("InMemoryFilmStorage") FilmStorage filmStorage,
                              @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @BeforeEach
    void initFilmController() {
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    void createValidFilm() {
        film = Film.builder()
                .name("Тест фильм")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        filmService.createFilm(film);
        assertEquals(film.getName(), filmService.getFilmById(film.getId()).getName());
    }

    @Test
    void updateFilm() {
        film = Film.builder()
                .name("Тест фильм")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        filmService.createFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("Тест фильм 2")
                .description("Тест описание 2")
                .duration(20)
                .releaseDate(LocalDate.of(1976, 12, 3))
                .build();
        film = film2;
        filmService.updateFilm(film);
        assertEquals(film.getDescription(), film2.getDescription());
    }

    @Test
    void createBlancNameFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        Set<ConstraintViolation<Film>> violations = this.validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createDescriptionLength200Film() {
        film = Film.builder()
                .name("Тест фильм")
                .description("Тест описание Тест описание Тест описание Тест описание Тест описание Тест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание" +
                        "Тест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описаниеТест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        Set<ConstraintViolation<Film>> violations = this.validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createDateReleaseMistakeFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1894, 12, 3))
                .build();
        Set<ConstraintViolation<Film>> violations = this.validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createDurationMistakeFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(0)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        Set<ConstraintViolation<Film>> violations = this.validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}

