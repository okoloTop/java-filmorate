package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    protected int assignmentId = 0;
    protected HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public ArrayList<Film> homePage() {
        log.debug("Получен запрос GET /films.");
        return getAllFilms();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        return createFilm(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        return updateFilm(film);
    }

    public Film createFilm(Film film) {
        isValid(film);
        film.setId(++assignmentId);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return film;
    }

    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void deleteFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            return;
        }
        films.remove(filmId);

    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм c id" + film.getId() + " не найден");
        }
        isValid(film);
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return films.get(film.getId());
    }

    public void isValid(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата выпуска фильма должна быть старше 28.12.1895");
        }
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым ");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}


