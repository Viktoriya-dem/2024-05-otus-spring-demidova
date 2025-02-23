package ru.otus.recipe.exception;

public class NotUserRecipeException extends RuntimeException {

    public NotUserRecipeException(String message) {
        super(message);
    }
}
