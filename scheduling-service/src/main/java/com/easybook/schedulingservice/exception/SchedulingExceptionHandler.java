package com.easybook.schedulingservice.exception;

import com.easybook.schedulingservice.dto.regulardto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class SchedulingExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorDto> handleException(ResponseStatusException exception) {
    ErrorDto incorrectData = new ErrorDto(exception.getMessage());

    return new ResponseEntity<>(incorrectData, exception.getStatusCode());
  }

  @ExceptionHandler
  public ResponseEntity<ErrorDto> handleException(Exception exception) {
    ErrorDto incorrectData = new ErrorDto(exception.getMessage());

    return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
  }
}
