package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    User createUser(User user);

    ArrayList<User> getAllUsers();

    User updateUser(User user);
    User getUserById (Integer id);

}
