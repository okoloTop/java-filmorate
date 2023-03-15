package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingInt(Film::getRate).reversed();

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
        List<Film> popularFilm = filmStorage.getAllFilms();
        popularFilm.sort(FILM_COMPARATOR);
        if (count > popularFilm.size()) {
            return popularFilm;
        } else {
            return popularFilm.subList(0, count);
        }
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

    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        return filmStorage.createFilm(film);
    }

    public ArrayList<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }
}
