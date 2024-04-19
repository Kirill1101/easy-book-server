package com.easybook.authservice.exception;

import com.easybook.authservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<ErrorDto> handleException(Exception exception) {
    ErrorDto incorrectData = new ErrorDto(exception.getMessage());

    return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
  }
}
