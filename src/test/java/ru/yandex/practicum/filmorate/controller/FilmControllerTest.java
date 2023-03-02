package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FilmControllerTest {
    InMemoryFilmStorage inMemoryFilmStorage;
    Film film;

    @BeforeEach
    void initFilmController() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
    }

    @Test
    void createValidFilm() {
        film = Film.builder()
                .name("Тест фильм")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        inMemoryFilmStorage.createFilm(film);
        assertEquals(1,inMemoryFilmStorage.getAllFilms().size());
    }
    @Test
    void updateFilm() {
        film = Film.builder()
                .name("Тест фильм")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        inMemoryFilmStorage.createFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("Тест фильм 2")
                .description("Тест описание 2")
                .duration(20)
                .releaseDate(LocalDate.of(1976, 12, 3))
                .build();
        film = film2;
        inMemoryFilmStorage.updateFilm(film);
        assertEquals(1,inMemoryFilmStorage.getAllFilms().size());
        assertEquals(film.getDescription(),film2.getDescription());
    }
    @Test
    void createBlancNameFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.createFilm(film);
        });
        assertNotNull(thrown.getMessage());
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
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.createFilm(film);
        });
        assertNotNull(thrown.getMessage());
    }
    @Test
    void createDateReleaseMistakeFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(20)
                .releaseDate(LocalDate.of(1894, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.createFilm(film);
        });
        assertNotNull(thrown.getMessage());
    }
    @Test
    void createDurationMistakeFilm() {
        film = Film.builder()
                .name("")
                .description("Тест описание")
                .duration(0)
                .releaseDate(LocalDate.of(1972, 12, 3))
                .build();
        Exception thrown = Assertions.assertThrows(ValidationException.class, () -> {
            inMemoryFilmStorage.createFilm(film);
        });
        assertNotNull(thrown.getMessage());
    }
    }

