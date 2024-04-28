package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ScheduleDto {
  @NotNull
  private UUID id;

  private String userCreatorLogin;

  private String title;

  private Long durationOfOneSlot;

  private List<ScheduleDateDto> availableDates;

  private List<ServiceDto> services;

  private List<AppointmentDto> appointments;
}
