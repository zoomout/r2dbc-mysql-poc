package com.zoomout.myservice.core.exceptions;

import lombok.Getter;

@Getter
public class DatabaseMappingException extends RuntimeException {

    public DatabaseMappingException(String message) {
        super(message);
    }

}
