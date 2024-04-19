package com.easybook.schedulingservice.dto.regulardto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class AppointmentDto {
  @NotNull
  private Long id;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime startTime;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime endTime;

  private List<ServiceDto> services;

  private Long duration;
}
