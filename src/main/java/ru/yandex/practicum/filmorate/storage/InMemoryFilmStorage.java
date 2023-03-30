package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    protected int assignmentId = 0;
    protected HashMap<Integer, Film> films = new HashMap<>();

    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingInt(Film::getRate).reversed();

    UserStorage userStorage;

    @Override
    public Film createFilm(Film film) {
        film.setId(++assignmentId);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return film;
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void deleteFilm(int filmId) {
        if (!films.containsKey(filmId)) {
            return;
        }
        films.remove(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NullPointerException("Фильм c id" + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм: {}; его ID: {}; всего фильмов в базе: {}", film.getName(), film.getId(), films.size());
        return films.get(film.getId());
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.get(id) == null) {
            throw new NullPointerException();
        }
        log.debug("Получен фильм c  ID: {}; ", id);
        return films.get(id);
    }

    @Override
    public void likeFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        if (!userStorage.getUserById(userId).getLikes().contains(id)) {
            film.setRate(film.getRate() + 1);
            userStorage.getUserById(userId).getLikes().add(id);
            log.debug("Пользователь c  ID: {}; удалил свой лайк фильму с ID: {}", userId, id);
        } else {
            throw new ValidationException("Вы уже ставили лайк этому фильму");
        }
    }

    @Override
    public List<Film> findAllPopular(Integer count) {
        log.debug("Получен список самых популярных фильмов, размер списка COUNT: {};", count);
        List<Film> popularFilm = getAllFilms();
        popularFilm.sort(FILM_COMPARATOR);
        if (count > popularFilm.size()) {
            return popularFilm;
        } else {
            return popularFilm.subList(0, count);
        }
    }

    @Override
    public void deleteLikeFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        if (userStorage.getUserById(userId).getLikes().contains(id)) {
            film.setRate(film.getRate() - 1);
            userStorage.getUserById(userId).getLikes().remove(id);
            log.debug("Пользователь c  ID: {}; удалил свой лайк фильму с ID: {}", userId, id);
        } else {
            throw new ValidationException("Вы не ставили лайк этому фильму");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        return null;
    }

    @Override
    public List<MPA> getAllMpa() {
        return null;
    }

    @Override
    public MPA getMpaById(Integer id) {
        return null;
    }
}
