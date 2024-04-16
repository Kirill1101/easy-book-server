package com.easybook.schedulingservice.entity;

import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
public class Appointment {
  @Id
  private Long id;

  private String userLogin;

  @Temporal(TemporalType.DATE)
  private LocalDate date;

  @Temporal(TemporalType.TIME)
  private LocalTime startTime;

  @Temporal(TemporalType.TIME)
  private LocalTime endTime;

  @ManyToMany
  private List<Service> services;

  @Type(PostgreSQLIntervalType.class)
  private Duration duration;

  @ManyToOne
  private Schedule schedule;
}
