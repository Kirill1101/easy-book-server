package com.easybook.schedulingservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Slot {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Temporal(TemporalType.TIME)
  private LocalTime startTime;

  @Temporal(TemporalType.TIME)
  private LocalTime endTime;

  private UUID appointmentId;

  @ManyToOne
  private ScheduleDate scheduleDate;
}
