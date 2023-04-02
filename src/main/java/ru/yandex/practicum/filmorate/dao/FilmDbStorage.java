package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Autowired
    public FilmDbStorage(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate,
                         GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(new MPA(rs.getInt("rating_id"), mpaStorage.getMpaName(rs.getInt("rating_id"))))
                .genres(genreStorage.getFilmGenres(rs.getInt("film_id")))
                .build();
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into films (NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATE, RATING_ID) " +
                " VALUES(? , ? , ? , ? , ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement prSt = connection.prepareStatement(
                            sql, new String[]{"film_id"});
                    prSt.setString(1, film.getName());
                    prSt.setString(2, film.getDescription());
                    prSt.setInt(3, film.getDuration());
                    prSt.setDate(4, Date.valueOf(film.getReleaseDate()));
                    prSt.setInt(5, film.getRate() == null ? 0 : film.getRate());
                    prSt.setInt(6, film.getMpa().getId());
                    return prSt;
                }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        genreStorage.saveGenres(film);
        log.debug("Создан фильм:{}.", film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM FILMS;";
        log.debug("Получен запрос на получение списка всех фильмов");
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            getFilmById(film.getId());
            String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASE_DATE = ?, RATING_ID = ? " +
                    " WHERE FILM_ID = ? ";
            jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    film.getDuration(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getMpa().getId(),
                    film.getId()
            );
            genreStorage.saveGenres(film);
        } catch (NullPointerException e) {
            throw new NullPointerException("Фильм не найден");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID = ?;";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        if (films.size() < 1) {
            throw new NullPointerException("Пользователя с таким id нет в базе");
        }
        return films.get(0);
    }

    public List<Film> findAllPopular(Integer count) {
        //log.debug("Получен список самых популярных фильмов, размер списка COUNT: {};", count);
        String sql = "SELECT * FROM FILMS ORDER BY RATE DESC";
        List<Film> popularFilm = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (count > popularFilm.size()) {
            return popularFilm;
        } else {
            return popularFilm.subList(0, count);
        }
    }

    @Override
    public void deleteLikeFilm(Integer id, Integer userId) {
        try {
            getFilmById(id);
            userStorage.getUserById(userId);
            String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
            jdbcTemplate.update(sql, id, userId);
            Film film = getFilmById(id);
            film.setRate(film.getRate() - 1);
            updateFilm(film);
        } catch (NullPointerException e) {
            throw new NullPointerException("Такого пользователя или фильма нет в базе");
        }
    }

    public void likeFilm(Integer id, Integer userId) {
        String sql = "insert into likes (FILM_ID, USER_ID) " +
                " VALUES(? , ?)";
        jdbcTemplate.update(sql, id, userId);
        log.debug("Пользователь c  ID: {}; поставил лайк фильму с ID: {}", userId, id);
        Film film = getFilmById(id);
        film.setRate(film.getRate() + 1);
        updateFilm(film);
    }
}
