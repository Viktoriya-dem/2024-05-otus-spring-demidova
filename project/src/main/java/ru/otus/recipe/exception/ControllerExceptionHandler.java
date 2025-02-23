package ru.otus.recipe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.recipe.dto.ErrorDto;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(NotFoundException exception) {
        return new ErrorDto(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception exception) {
        exception.printStackTrace();

        return new ErrorDto(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotUserRecipeException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleException(NotUserRecipeException exception) {
        exception.printStackTrace();

        return new ErrorDto(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

}