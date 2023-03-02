package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;

    private final FilmService filmService;

    @GetMapping("/films")
    public ArrayList<Film> homePage() {
        log.debug("Получен запрос GET /films.");
        return filmStorage.getAllFilms();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        return filmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        return filmStorage.updateFilm(film);
    }
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Integer filmId) {
        log.debug("Получен запрос GET /films/{id}.");
        return filmStorage.getFilmById(filmId);
    }
    @GetMapping("/films/popular")
    public List<Film> findAll(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        if(count <= 0){
            throw new IllegalArgumentException();
        }
        return filmService.findAllPopular(count);
    }
    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.debug("Получен запрос PUT /films/{id}/like/{userId}.");
        filmService.likeFilm(filmId,userId);
    }
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.debug("Получен запрос DELETE /films/{id}/like/{userId}.");
        filmService.deleteLikeFilm(filmId,userId);
    }
}


