package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {FilmController.class, UserController.class, GenreController.class, MpaController.class})

public class ErrorHandler {
    private ErrorResponse errorResponse;

    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final Exception e) {
        return new ErrorResponse("Ошибка валидации данных: " + e.getMessage());
    }

    @ExceptionHandler({
            NullPointerException.class,
            })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllError(final Exception e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

    private static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
