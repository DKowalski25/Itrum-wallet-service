package org.example.wallet.dto;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiError(
        String message,
        HttpStatus statusCode,
        ZonedDateTime timeStamp
) {}
