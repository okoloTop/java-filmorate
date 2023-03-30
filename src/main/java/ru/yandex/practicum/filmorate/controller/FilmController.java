package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> homePage() {
        log.debug("Получен запрос GET /films.");
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public Film create(@RequestBody @Valid Film film) {
        log.debug("Получен запрос POST /films.");
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film film) {
        log.debug("Получен запрос PUT /films.");
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Integer filmId) {
        log.debug("Получен запрос GET /films/{id}.");
        return filmService.getFilmById(filmId);
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
    @GetMapping("/genres")
    public List<Genre> getGenres() {
        log.debug("Получен запрос GET /genres.");
        return filmService.getAllGenres();
    }
    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") Integer genreId) {
        log.debug("Получен запрос GET /genres/{id}.");
        return filmService.getGenreById(genreId);
    }

    @GetMapping("/mpa")
    public List<MPA> getMPA() {
        log.debug("Получен запрос GET /mpa.");
        return filmService.getAllMpa();
    }
    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable("id") Integer genreId) {
        log.debug("Получен запрос GET /mpa/{id}.");
        return filmService.getMpaById(genreId);
    }
}


