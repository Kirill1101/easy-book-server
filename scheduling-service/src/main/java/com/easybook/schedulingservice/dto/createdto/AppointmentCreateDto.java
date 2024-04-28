package com.easybook.schedulingservice.dto.createdto;

import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AppointmentCreateDto {
  private LocalDate date;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime startTime;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime endTime;

  private List<ServiceDto> services;

  private Long duration;

  private UUID scheduleId;
}
