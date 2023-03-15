package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    protected int assignmentId = 0;
    protected HashMap<Integer, Film> films = new HashMap<>();

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

}
