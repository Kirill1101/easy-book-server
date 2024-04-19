package com.easybook.schedulingservice.dto.regulardto;

import lombok.Data;

@Data
public class ErrorDto {
  private String message;

  public ErrorDto(String message) {
    this.message = message;
  }
}
