package com.easybook.authservice.dto;

import lombok.Data;

@Data
public class ErrorDto {
  private String info;

  public ErrorDto(String info) {
    this.info = info;
  }
}
