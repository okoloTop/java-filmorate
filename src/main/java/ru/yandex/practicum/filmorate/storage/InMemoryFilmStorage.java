package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    protected int assignmentId = 0;
    protected HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        valid(film);
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
        valid(film);
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

    private void valid(Film film) {
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
