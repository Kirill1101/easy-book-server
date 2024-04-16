package com.easybook.schedulingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Entity
@Data
public class Slot {
  @Id
  private Long id;

  private LocalDate localDate;

  private LocalTime startTime;

  private LocalTime endTime;

  private Long appointmentId;

  @ManyToOne
  private Schedule schedule;
}
