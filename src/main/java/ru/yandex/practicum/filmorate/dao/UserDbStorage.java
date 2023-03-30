package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = User.builder()
                .id(rs.getInt("user_id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        return user;
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                " VALUES(? , ? , ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement prSt = connection.prepareStatement(
                            sql
                            , new String[]{"user_id"});
                    prSt.setString(1, user.getEmail());
                    prSt.setString(2, user.getLogin());
                    prSt.setString(3, user.getName());
                    prSt.setDate(4, Date.valueOf(user.getBirthday()));
                    return prSt;
                }
                , keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERS;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User updateUser(User user) {
        try {
            getUserById(user.getId());
            String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                    " WHERE USER_ID = ?;";

            jdbcTemplate.update(sql
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , Date.valueOf(user.getBirthday())
                    , user.getId()
            );
            return user;
        } catch (ValidationException exception) {
            throw new ValidationException("Такого пользователя нет в базе");
        }
    }

    @Override
    public User getUserById(Integer id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ? ;";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (users.size() < 1) {
            throw new NullPointerException("Пользователя с таким id нет в базе");
        }
        return users.get(0);
    }

    public List<User> getCommonFriend(Integer userId, Integer friendId) {
        List<User> userFriend = getAllFriend(userId);
        List<User> friendFriend = getAllFriend(friendId);
        List<User> commonFriend = new ArrayList<>();
        for (User u : userFriend) {
            if (friendFriend.contains(u)) {
                commonFriend.add(u);
            }
        }
        log.debug("Получен список общих друзей пользователя c  ID: {}; и пользователя с ID: {}", userId, friendId);
        return commonFriend;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        try {
            getUserById(userId);
            getUserById(friendId);
            String sql = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, userId, friendId, "CONFIRMED");
        } catch (NullPointerException e) {
            throw new NullPointerException("Пользователя с таким id нет в базе");
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        try {
            getUserById(userId);
            getUserById(friendId);
            String sql = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?;";
            jdbcTemplate.update(sql, userId, friendId);
        } catch (NullPointerException e) {
            throw new NullPointerException("Пользователя с таким id нет в базе");
        }

    }

    @Override
    public List<User> getAllFriend(Integer userId) {
        try {
            getUserById(userId);
            String sql = "SELECT * FROM USERS " +
                    " WHERE USER_ID IN " +
                    " (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? AND STATUS = 'CONFIRMED');";

            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
        } catch (NullPointerException e) {
            throw new NullPointerException("Пользователя с таким id нет в базе");
        }
    }
}
