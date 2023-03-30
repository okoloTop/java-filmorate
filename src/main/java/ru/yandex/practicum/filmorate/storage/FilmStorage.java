package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmById(Integer id);

    public void likeFilm(Integer id, Integer userId);

    public List<Film> findAllPopular(Integer count);

    public void deleteLikeFilm(Integer id, Integer userId);

    List<Genre> getAllGenres();

    Genre getGenreById(Integer genreId);

    List<MPA> getAllMpa();

    MPA getMpaById(Integer id);
}
