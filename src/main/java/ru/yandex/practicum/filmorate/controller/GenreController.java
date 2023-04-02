package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

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
}
