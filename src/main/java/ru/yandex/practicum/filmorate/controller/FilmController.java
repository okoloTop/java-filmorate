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

    public Film createFilm(Film film) {
        if (isValid(film)) {
            film.setId(++assignmentId);
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Фильм " + film.getName() + " не будет добавлен,проверьте правильность введенных данных");
        }
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
        if (isValid(film)) {
            films.put(film.getId(), film);
            return films.get(film.getId());
        } else {
            throw new ValidationException("Фильм " + film.getName() + " не будет добавлен,проверьте правильность введенных данных");
        }
    }

    public boolean isValid(Film film) {
        Boolean valid = true;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return valid = false;
        }
        if (film.getName().isBlank()) {
            return valid = false;
        }
        if (film.getDescription().length() > 200) {
            return valid = false;
        }
        if (film.getDuration() <= 0) {
            return valid = false;
        }
        return valid;
    }

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
}


