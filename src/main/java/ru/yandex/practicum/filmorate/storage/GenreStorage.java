package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public List<Genre> getFilmGenres(int filmId);

    public List<Genre> getAllGenres();

    public Genre getGenreById(Integer genreId);

    public void saveGenres(Film film);


}
