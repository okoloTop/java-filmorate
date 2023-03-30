package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserById (Integer id);

    public List<User> getCommonFriend(Integer userId, Integer friendId);

    public void addFriend(Integer userId, Integer friendId);

    public void deleteFriend(Integer userId, Integer friendId);

    public List<User> getAllFriend(Integer userId);


}
