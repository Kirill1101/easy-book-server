package com.easybook.schedulingservice.dto.createdto;

import com.easybook.schedulingservice.entity.Service;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class AppointmentCreateDto {
  private LocalTime startTime;

  private LocalTime endTime;

  private List<Service> services;

  private Duration duration;

  private Long scheduleId;
}
