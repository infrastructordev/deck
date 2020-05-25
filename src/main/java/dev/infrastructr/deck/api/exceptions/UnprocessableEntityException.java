package dev.infrastructr.deck.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseStatus(value = UNPROCESSABLE_ENTITY, reason = UnprocessableEntityException.REASON)
public class UnprocessableEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    static final String REASON = "Can not process entity.";
}
