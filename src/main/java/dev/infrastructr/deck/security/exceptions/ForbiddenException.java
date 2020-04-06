package dev.infrastructr.deck.security.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(value = FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenException(String msg){
        super(msg);
    }
}
