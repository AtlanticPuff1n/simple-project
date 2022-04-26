package com.demo.student.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;
    private final String referenceId;
}
