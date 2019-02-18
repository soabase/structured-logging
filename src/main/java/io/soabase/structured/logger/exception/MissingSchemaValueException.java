package io.soabase.structured.logger.exception;

public class MissingSchemaValueException extends RuntimeException {
    public MissingSchemaValueException(String message) {
        super(message);
    }
}
