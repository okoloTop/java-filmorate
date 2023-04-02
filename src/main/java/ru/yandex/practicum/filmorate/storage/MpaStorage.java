package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {

    public List<MPA> getAllMpa();

    public MPA getMpaById(Integer id);

    public String getMpaName(Integer id);
}
