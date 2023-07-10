package ru.practicum.exceptions;

public enum ExceptionMessages {
    NOT_FOUND_EXCEPTION("object not found"),
    CONFLICT_EXCEPTION("conflict exception"),
    VALIDATE_EXCEPTION("validate exception");

    public final String label;

    ExceptionMessages(String label) {
        this.label = label;
    }
}