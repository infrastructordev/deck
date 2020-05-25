package dev.infrastructr.deck.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(value = INTERNAL_SERVER_ERROR, reason = InternalServerError.REASON)
public class InternalServerError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    static final String REASON = "Internal server error.";
}
