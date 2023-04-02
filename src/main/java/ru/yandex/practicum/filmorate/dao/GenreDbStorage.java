package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID IN (SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?) " +
                " ORDER BY GENRE_ID;";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME")), filmId);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID;";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME")));
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ? ;";
        List<Genre> genres = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME")), genreId);
        if (genres.size() < 1) {
            throw new NullPointerException("Жанр с таким id не найден");
        } else {
            return genres.get(0);
        }
    }

    @Override
    public void saveGenres(Film film) {
        int filmId = film.getId();
        if (getFilmGenres(filmId).size() > 0) {
            String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ? ;";
            jdbcTemplate.update(sql, filmId);
        }
        if (film.getGenres() != null) {
            final Set<Integer> filmGenresId = new HashSet<>();
            for (Genre g : film.getGenres()) {
                filmGenresId.add(g.getId());
            }
            String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Integer genreId : filmGenresId) {
                jdbcTemplate.update(sql, filmId, genreId);
            }
        }
        film.setGenres(getFilmGenres(film.getId()));
    }
}
