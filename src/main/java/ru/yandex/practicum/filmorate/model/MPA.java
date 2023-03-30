package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MPA {
    private Integer id;
    private String name;

    public MPA(int id) {
        this.id = id;
    }
}