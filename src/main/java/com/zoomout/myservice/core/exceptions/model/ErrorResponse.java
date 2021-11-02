package com.zoomout.myservice.core.exceptions.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    String message;
}
