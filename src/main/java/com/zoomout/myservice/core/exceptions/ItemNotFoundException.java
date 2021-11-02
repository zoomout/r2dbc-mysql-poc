package com.zoomout.myservice.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ItemNotFoundException extends RuntimeException {

    private final HttpStatus statusCode;

    public ItemNotFoundException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
