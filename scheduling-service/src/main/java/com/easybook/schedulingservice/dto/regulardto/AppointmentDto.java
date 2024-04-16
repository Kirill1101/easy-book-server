package com.easybook.schedulingservice.dto.regulardto;

import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Service;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

public class AppointmentDto {
  @NotNull
  private Long id;

  private String userLogin;

  private LocalTime startTime;

  private LocalTime endTime;

  private List<Service> services;

  private Instant duration;

  private Schedule schedule;
}
