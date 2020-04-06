package dev.infrastructr.deck.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = ResourceRenderingException.REASON)
public class ResourceRenderingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    static final String REASON = "Resource rendering has failed.";

    public ResourceRenderingException(String msg, Throwable cause){
        super(msg, cause);
    }
}
