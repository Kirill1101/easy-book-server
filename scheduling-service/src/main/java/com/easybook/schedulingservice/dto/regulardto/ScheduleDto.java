package com.easybook.schedulingservice.dto.regulardto;

import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ScheduleDto {
  @NotNull
  private Long id;

  private String userCreatorLogin;

  private String title;

  private Long durationOfOneSlot;

  private List<ScheduleDateDto> availableDates;

  private List<ServiceDto> services;

  private List<AppointmentDto> appointments;
}
