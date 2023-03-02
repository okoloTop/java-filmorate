package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
    Film createFilm(Film film);

    ArrayList<Film> getAllFilms();

    Film updateFilm(Film film);
    Film getFilmById(Integer id);

}
