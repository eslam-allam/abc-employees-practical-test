package com.abc.employees.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ErrorDto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private final String message;
    private final Map<String, String> validationErrors;

    private ErrorDto(String message, Map<String, String> validationErrors) {
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String message;
        private Map<String, String> validationErrors;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public ErrorDto build() {
            return new ErrorDto(message, validationErrors);
        }
    }

}
