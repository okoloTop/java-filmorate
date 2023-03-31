package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT * FROM RATINGS";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new MPA(rs.getInt("RATING_ID"), rs.getString("NAME")));
    }

    @Override
    public MPA getMpaById(Integer id) {
        String sql = "SELECT * FROM RATINGS WHERE RATING_ID = ? ;";
        List<MPA> mpa = jdbcTemplate.query(sql,
                (rs, rowNum) -> new MPA(rs.getInt("RATING_ID"), rs.getString("NAME")), id);
        if (mpa.size() < 1) {
            throw new NullPointerException("Жанр с таким id не найден");
        }
        return mpa.get(0);
    }

    @Override
    public String getMpaName(Integer id) {
        SqlRowSet nameRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATINGS WHERE RATING_ID = ?", id);
        String name;
        if (nameRows.next()) {
            name = nameRows.getString("name");
            return name;
        } else {
            return null;
        }
    }
}
