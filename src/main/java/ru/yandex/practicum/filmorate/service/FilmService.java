package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;

    }

    public void likeFilm(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        if (!userStorage.getUserById(userId).getLikes().contains(id)) {
            film.setRate(film.getRate() + 1);
            userStorage.getUserById(userId).getLikes().add(id);
            log.debug("Пользователь c  ID: {}; удалил свой лайк фильму с ID: {}", userId, id);
        } else {
            throw new ValidationException("Вы уже ставили лайк этому фильму");
        }
    }

    public List<Film> findAllPopular(Integer count) {
        log.debug("Получен список самых популярных фильмов, размер списка COUNT: {};", count);
        return filmStorage.getAllFilms().stream().sorted((p0, p1) -> {
            int comp = -1 * p0.getRate().compareTo(p1.getRate());
            return comp;
        }).limit(count).collect(Collectors.toList());

    }

    public void deleteLikeFilm(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        if (userStorage.getUserById(userId).getLikes().contains(id)) {
            film.setRate(film.getRate() - 1);
            userStorage.getUserById(userId).getLikes().remove(id);
            log.debug("Пользователь c  ID: {}; удалил свой лайк фильму с ID: {}", userId, id);
        } else {
            throw new ValidationException("Вы не ставили лайк этому фильму");
        }
    }
}
