package com.example.rideshare.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .orElse("Invalid input");

        return new ApiError(400, msg, req.getRequestURI());
    }

    // 404 errors
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return new ApiError(404, ex.getMessage(), req.getRequestURI());
    }

    // 400 errors
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        return new ApiError(400, ex.getMessage(), req.getRequestURI());
    }
}
