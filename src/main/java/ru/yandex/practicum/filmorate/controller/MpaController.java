package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MpaController {
    private final FilmService filmService;

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
