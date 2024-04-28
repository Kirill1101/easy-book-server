package com.easybook.schedulingservice.dto.createdto;

import com.easybook.schedulingservice.dto.regulardto.AppointmentDto;
import com.easybook.schedulingservice.entity.Appointment;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ScheduleCreateDto {
  @NotNull
  private String title;

  private Long durationOfOneSlot;

  private List<ScheduleDateCreateDto> availableDates;

  private List<ServiceCreateDto> services;

  private UUID organizationsId;
}
