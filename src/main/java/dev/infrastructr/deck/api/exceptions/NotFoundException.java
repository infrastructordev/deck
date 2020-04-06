package dev.infrastructr.deck.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = NotFoundException.REASON)
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    static final String REASON = "Not allowed to read/update resource.";
}
