package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Validated
public class User {
    private Integer id;

    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @NotBlank(message = "Это поле не может быть пустым")
    @Email(message = "Введенное значение не является адресом электронной почты.")
    private String email;

    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    private LocalDate birthday;

    private Set<Integer> likes;

    private Set<Integer> friends;

}
